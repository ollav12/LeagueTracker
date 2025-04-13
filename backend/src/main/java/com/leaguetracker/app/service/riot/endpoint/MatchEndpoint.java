package com.leaguetracker.app.service.riot.endpoint;

import org.springframework.stereotype.Service;

import com.leaguetracker.app.dto.MatchDto;
import com.leaguetracker.app.dto.response.RiotMatchListResponse;
import com.leaguetracker.app.helper.Helper;
import com.leaguetracker.app.mapper.RiotMatchMapper;
import com.leaguetracker.app.service.riot.RiotRequest;

@Service
public class MatchEndpoint {

    private final String apiKey;

    public MatchEndpoint(String apiKey) {
        this.apiKey = apiKey;
    }

    public MatchDto findByMatchId(String matchId, String region) {
        String endpoint = "lol/match/v5/matches/" + matchId + "?api_key=";
        RiotRequest<MatchDto> request = new RiotRequest<>(
                Helper.getRiotApiRegion(region),
                endpoint,
                apiKey,
                RiotMatchMapper.INSTANCE::apply);
        return request.execute();
    }

    public RiotMatchListResponse findByPuuid(String puuid, String region, int start, int count) {
        String endpoint = "lol/match/v5/matches/by-puuid/" + puuid + "/ids?start=" + start + "&count=" + count
                + "&api_key=";
        RiotRequest<RiotMatchListResponse> request = new RiotRequest<>(
                Helper.getRiotApiRegion(region),
                endpoint,
                apiKey,
                RiotMatchMapper.INSTANCE::toRiotMatchListResponse);
        return request.execute();
    }
}
