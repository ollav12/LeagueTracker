package com.leaguetracker.app.service;

import com.leaguetracker.app.dto.request.SummonerLookupRequest;
import com.leaguetracker.app.dto.request.SummonerSummaryRequest;
import com.leaguetracker.app.dto.response.*;
import com.leaguetracker.app.mapper.RiotSummonerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SummonerInfoService {

    private final SummonerService summonerService;
    private final MatchService matchService;
    private final AccountService accountService;
    private final RankService rankService;
    private final StatsService statsService;


    public SummonerLookupResponse getSummonerDetails(SummonerLookupRequest request) {
        RiotAccountResponse account = accountService.getAccount(
                request.region(),
                request.summonerName(),
                request.tag());
        RiotSummonerResponse summoner = summonerService.getSummoner(
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
                MatchService.MatchListMode.LIGHT);

        return RiotSummonerMapper.INSTANCE.toSummonerLookupResponse(
                account,
                summoner,
                league);
    }

    public SummonerSummaryResponse getSummary(SummonerSummaryRequest request) {

        List<String> matchIds = matchService.getNextMatchIds(
                request.puuid(),
                request.lastMatchId(),
                request.limit());
        List<RiotMatchResponse> matchDetails = matchService.getMatches(
                request.region(),
                matchIds);
        Map<String, Object> stats = statsService.getSummonerStats(request.puuid());

        return SummonerSummaryResponse.builder()
                .matchIds(matchService.getNextMatchIds(request.puuid(), request.lastMatchId(), request.limit()))
                .matchDetails(matchDetails)
                .stats(stats)
                .build();
    }
}
