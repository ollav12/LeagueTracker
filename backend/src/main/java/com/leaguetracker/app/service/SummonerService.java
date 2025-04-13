package com.leaguetracker.app.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.leaguetracker.app.dto.RiotLeagueEntry;
import com.leaguetracker.app.dto.RiotLeagueEntry.MiniSeriesDto;
import com.leaguetracker.app.dto.request.SummonerLookupRequest;
import com.leaguetracker.app.dto.request.SummonerSummaryRequest;
import com.leaguetracker.app.dto.MatchDto;
import com.leaguetracker.app.dto.response.RiotAccountResponse;
import com.leaguetracker.app.dto.response.RiotLeagueResponse;
import com.leaguetracker.app.dto.response.RiotSummonerResponse;
import com.leaguetracker.app.dto.response.SummonerLookupResponse;
import com.leaguetracker.app.dto.response.SummonerSummaryResponse;
import com.leaguetracker.app.mapper.RiotSummonerMapper;
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
    private final AccountService accountService;

    public SummonerService(SummonerRepository summonerRepository, RiotService riotService, RankService rankService,
            MatchService matchService, StatsService statsService, AccountService accountService) {
        this.summonerRepository = summonerRepository;
        this.rankService = rankService;
        this.riotService = riotService;
        this.matchService = matchService;
        this.statsService = statsService;
        this.accountService = accountService;
    }

    /**
     * Get a summoner
     * 
     * @param puuid
     * @return summoner response
     */
    public SummonerLookupResponse getSummonerDetails(SummonerLookupRequest request) {
        RiotAccountResponse account = accountService.getAccount(
                request.region(),
                request.summonerName(),
                request.tag());
        RiotSummonerResponse summoner = this.getSummoner(account.puuid(), account.gameName(), request.region());
        RiotLeagueResponse league = rankService.getRanked(summoner.puuid(), request.region());

        // Fetch first 100 matches
        matchService.updateMatchList(account.puuid(), request.region(), MatchListMode.LIGHT);

        return RiotSummonerMapper.INSTANCE.toResponse(
                request.summonerName(),
                request.tag(),
                summoner,
                league);
    }

    public RiotSummonerResponse getSummoner(String puuid, String name, String tag) {
        Summoner summoner = summonerRepository.findById(puuid).orElse(null);

        if (summoner != null) {
            System.out.println("Retrieved summoner from database: " + summoner.getSummonerName());
            return RiotSummonerResponse.builder()
                    .id(summoner.getId())
                    .accountId(summoner.getAccountId())
                    .puuid(summoner.getPuuid())
                    .profileIconId(summoner.getProfileIconId())
                    .revisionDate(summoner.getRevisionDate())
                    .summonerLevel(summoner.getSummonerLevel())
                    .build();
        }
        System.out.println("Summoner not found in database, fetching from Riot API");
        RiotSummonerResponse riotSummoner = riotService.Summoner.findByPuuid(puuid, tag);

        if (riotSummoner != null) {
            Summoner newSummoner = Summoner.builder()
                    .puuid(riotSummoner.puuid())
                    .id(riotSummoner.id())
                    .accountId(riotSummoner.accountId())
                    .summonerName(name)
                    .region(tag)
                    .profileIconId(riotSummoner.profileIconId())
                    .revisionDate(riotSummoner.revisionDate())
                    .summonerLevel(riotSummoner.summonerLevel())
                    .lastUpdated(new Date())
                    .build();

            summonerRepository.save(newSummoner);
            System.out.println("Fetched summoner from Riot API and saved to database: " + name);
        }
        return riotSummoner;
    }

    /**
     * Get account by puuid
     * 
     * @return AccountDto
     */
    public RiotAccountResponse getAccountByPuuid(String puuid, String region) {
        return riotService.Account.findByPuuid(puuid, region);
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

    public SummonerSummaryResponse getSummary(SummonerSummaryRequest request) {

        List<String> matchIds = matchService.getNextMatchIds(request.puuid(), request.lastMatchId(), request.limit());

        List<MatchDto> matchDetails = matchService.getMatches(request.region(), matchIds);

        Map<String, Object> stats = statsService.getSummonerStats(request.puuid());

        return SummonerSummaryResponse.builder()
                .matchIds(matchService.getNextMatchIds(request.puuid(), request.lastMatchId(), request.limit()))
                .matchDetails(matchDetails)
                .stats(stats)
                .build();
    }

    public Summoner saveSummoner(Summoner summoner) {
        return summonerRepository.save(summoner);
    }

    public List<Summoner> getAllSummoners() {
        return summonerRepository.findAll();
    }
}
