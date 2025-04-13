package com.leaguetracker.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.leaguetracker.app.dto.request.SummonerLookupRequest;
import com.leaguetracker.app.dto.request.SummonerSummaryRequest;
import com.leaguetracker.app.dto.response.RiotAccountResponse;
import com.leaguetracker.app.dto.response.RiotLeagueResponse;
import com.leaguetracker.app.dto.response.RiotMatchResponse;
import com.leaguetracker.app.dto.response.RiotSummonerResponse;
import com.leaguetracker.app.dto.response.SummonerLookupResponse;
import com.leaguetracker.app.dto.response.SummonerSummaryResponse;
import com.leaguetracker.app.mapper.RiotSummonerMapper;
import com.leaguetracker.app.model.Summoner;
import com.leaguetracker.app.repository.SummonerRepository;
import com.leaguetracker.app.service.MatchService.MatchListMode;
import com.leaguetracker.app.service.riot.RiotService;

@Slf4j
@Service
@RequiredArgsConstructor
public class SummonerService {
    private final SummonerRepository summonerRepository;
    private final RiotService riotService;
    private final RankService rankService;
    private final MatchService matchService;
    private final StatsService statsService;
    private final AccountService accountService;

    public SummonerLookupResponse getSummonerDetails(SummonerLookupRequest request) {
        RiotAccountResponse account = accountService.getAccount(
                request.region(),
                request.summonerName(),
                request.tag());
        RiotSummonerResponse summoner = this.getSummoner(
                account.puuid(),
                account.gameName(),
                request.region());
        RiotLeagueResponse league = rankService.getRanked(
                summoner.puuid(),
                request.region());

        // Fetch first 100 matches
        matchService.updateMatchList(
                account.puuid(),
                request.region(),
                MatchListMode.LIGHT);

        return RiotSummonerMapper.INSTANCE.toSummonerLookupResponse(
                account,
                summoner,
                league);
    }

    public SummonerSummaryResponse getSummary(SummonerSummaryRequest request) {

        List<String> matchIds = matchService.getNextMatchIds(request.puuid(), request.lastMatchId(), request.limit());

        List<RiotMatchResponse> matchDetails = matchService.getMatches(request.region(), matchIds);

        Map<String, Object> stats = statsService.getSummonerStats(request.puuid());

        return SummonerSummaryResponse.builder()
                .matchIds(matchService.getNextMatchIds(request.puuid(), request.lastMatchId(), request.limit()))
                .matchDetails(matchDetails)
                .stats(stats)
                .build();
    }

    public RiotSummonerResponse getSummoner(String puuid, String name, String tag) {
        Summoner summoner = summonerRepository.findById(puuid).orElse(null);

        if (summoner != null) {
            System.out.println("Retrieved summoner from database: " + summoner.getSummonerName());
            return RiotSummonerMapper.INSTANCE.toRiotSummonerResponse(summoner);
        }
        System.out.println("Summoner not found in database, fetching from Riot API");
        RiotSummonerResponse riotSummoner = riotService.Summoner.findByPuuid(puuid, tag);

        if (riotSummoner != null) {
            Summoner newSummoner = RiotSummonerMapper.INSTANCE.toSummoner(riotSummoner);
            summonerRepository.save(newSummoner);
            System.out.println("Fetched summoner from Riot API and saved to database: " + name);
        }
        return riotSummoner;
    }

    public Summoner saveSummoner(Summoner summoner) {
        return summonerRepository.save(summoner);
    }
}
