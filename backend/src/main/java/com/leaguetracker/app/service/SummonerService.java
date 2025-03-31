package com.leaguetracker.app.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.leaguetracker.app.dto.AccountDto;
import com.leaguetracker.app.dto.LeagueDto;
import com.leaguetracker.app.dto.LeagueDto.MiniSeriesDto;
import com.leaguetracker.app.dto.MatchDto;
import com.leaguetracker.app.dto.SummonerDto;
import com.leaguetracker.app.dto.response.SummonerResponse;
import com.leaguetracker.app.mapper.SummonerMapper;
import com.leaguetracker.app.model.MatchList;
import com.leaguetracker.app.model.Summoner;
import com.leaguetracker.app.model.SummonerRank;
import com.leaguetracker.app.repository.SummonerRepository;
import com.leaguetracker.app.service.MatchService.MatchListMode;
import com.leaguetracker.app.service.riot.RiotService;

@Service
public class SummonerService {
    private final SummonerRepository summonerRepository;

    private final RiotService riotService;
    private final RankService rankService;
    private final MatchService matchService;
    private final StatsService statsService;

    public SummonerService(SummonerRepository summonerRepository, RiotService riotService, RankService rankService,
            MatchService matchService, StatsService statsService) {
        this.summonerRepository = summonerRepository;
        this.rankService = rankService;
        this.riotService = riotService;
        this.matchService = matchService;
        this.statsService = statsService;
    }

    /**
     * Get a summoner
     * 
     * @param puuid
     * @return summoner response
     */
    public SummonerResponse getSummonerDetails(String summonerName, String region, String tag) {
        AccountDto account = this.getAccount(region, summonerName, tag);
        System.out.println("Fetched account form riot api: " + account);

        if (account != null) {
            SummonerDto summoner = this.getSummoner(account.puuid());
            if (summoner != null) {
                System.out.println("Retrieved summoner from database: " + summoner);

                List<LeagueDto> ranked = this.getRanked(summoner.puuid(), region);

                System.out.println("Retrieved ranked data from database: " + ranked);

                return SummonerMapper.toResponse(summonerName, tag, summoner, ranked);
            }
            SummonerDto summonerDto = riotService.Summoner.findByPuuid(account.puuid(), region);
            System.out.println("Fetched summoner from riot api: " + summonerDto);

            List<LeagueDto> ranked = riotService.League.findBySummonerId(summonerDto.id(), region);
            System.out.println("Fetched ranked data from riot api: " + ranked);

            // Fetch first 100 matches
            matchService.updateMatchList(account.puuid(), region, MatchListMode.LIGHT);

            SummonerResponse response = SummonerMapper.toResponse(summonerName, tag, summonerDto, ranked);
            Summoner newSummoner = SummonerMapper.toEntity(region, response);
            summonerRepository.save(newSummoner);

            return response;
        }

        return null;
    }

    /**
     * Get summoner from database by puuid
     * 
     * @param puuid
     * @param region
     * @return SummonerDto
     */
    public SummonerDto getSummoner(String puuid) {
        Summoner summoner = summonerRepository.findById(puuid).orElse(null);
        if (summoner != null) {
            return new SummonerDto(
                    summoner.getId(),
                    summoner.getAccountId(),
                    summoner.getPuuid(),
                    summoner.getProfileIconId(),
                    summoner.getRevisionDate(),
                    summoner.getSummonerLevel());
        }
        return null;
    }

    /**
     * Get account by puuid
     * 
     * @return AccountDto
     */
    public AccountDto getAccountByPuuid(String puuid, String region) {
        return riotService.Account.findByPuuid(puuid, region);
    }

    /**
     * Get account by region, summonerName, tag
     * 
     * @return AccountDto
     */
    public AccountDto getAccount(String region, String summonerName, String tag) {
        return riotService.Account.findByRiotId(region, summonerName, tag);
    }

    /**
     * Get summoner ranked data from database
     * 
     * @param summoner
     * @return
     */
    public List<LeagueDto> getRanked(String puuid, String region) {
        List<SummonerRank> ranked = rankService.getRankByPuuid(puuid);
        return ranked.stream()
                .map(rank -> new LeagueDto(
                        rank.getLeagueId(),
                        rank.getQueueType(),
                        rank.getTier(),
                        rank.getRank(),
                        rank.getSummonerId(),
                        rank.getPuuid(),
                        rank.getLeaguePoints(),
                        rank.getWins(),
                        rank.getLosses(),
                        rank.getVeteran(),
                        rank.getInactive(),
                        rank.getFreshBlood(),
                        rank.getHotStreak(),
                        new MiniSeriesDto(rank.getMiniSeries().getWins(), rank.getMiniSeries().getProgress(),
                                rank.getMiniSeries().getTarget(), rank.getMiniSeries().getLosses())))
                .collect(Collectors.toList());
    }

    /**
     * Get summary
     * 
     * @param summoner
     * @return
     */
    public List<MatchList> getSummary(String puuid, String accountId, String region) {
        return matchService.getMatchListByPuuid(puuid);
    }

    public Map<String, Object> getSummary(String puuid, String region, String lastMatchId, int limit) {
        Map<String, Object> summary = new HashMap<>();

        // Fetch next match IDs
        List<String> matchIds = matchService.getNextMatchIds(puuid, lastMatchId, limit);
        summary.put("matchIds", matchIds);

        // Fetch match details
        List<MatchDto> matchDetails = matchService.getMatches(region, matchIds);
        summary.put("matchDetails", matchDetails);

        // Fetch additional stats (if needed)
        Map<String, Object> stats = statsService.getSummonerStats(puuid);
        summary.put("stats", stats);

        return summary;
    }

    public Summoner saveSummoner(Summoner summoner) {
        return summonerRepository.save(summoner);
    }

    public List<Summoner> getAllSummoners() {
        return summonerRepository.findAll();
    }
}
