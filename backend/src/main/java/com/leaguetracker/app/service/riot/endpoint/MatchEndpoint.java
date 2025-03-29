package com.leaguetracker.app.service.riot.endpoint;

import org.springframework.stereotype.Service;

import com.leaguetracker.app.dto.MatchDto;
import com.leaguetracker.app.helper.Helper;
import com.leaguetracker.app.mapper.MatchMapper;
import com.leaguetracker.app.service.riot.RiotRequest;

@Service
public class MatchEndpoint {

    private final String apiKey;

    public MatchEndpoint(String apiKey) {
        this.apiKey = apiKey;
    }

    public MatchDto findByMatchId(String matchId, String region) {
        String endpoint = "lol/match/v5/matches/" + matchId;
        RiotRequest<MatchDto> request = new RiotRequest<>(Helper.getRiotApiRegion(region), endpoint, apiKey,
                new MatchMapper());
        return request.execute();
    }
}
