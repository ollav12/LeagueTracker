package com.leaguetracker.app.service.riot.endpoint;

import java.util.List;

import org.springframework.stereotype.Service;

import com.leaguetracker.app.dto.MatchDto;
import com.leaguetracker.app.dto.MatchListDto;
import com.leaguetracker.app.helper.Helper;
import com.leaguetracker.app.mapper.MatchListMapper;
import com.leaguetracker.app.mapper.MatchMapper;
import com.leaguetracker.app.service.riot.RiotRequest;

@Service
public class MatchEndpoint {

    private final String apiKey;

    public MatchEndpoint(String apiKey) {
        this.apiKey = apiKey;
    }

    public MatchDto findByMatchId(String matchId, String region) {
        String endpoint = "lol/match/v5/matches/" + matchId + "?api_key=";
        RiotRequest<MatchDto> request = new RiotRequest<>(Helper.getRiotApiRegion(region), endpoint, apiKey,
                new MatchMapper());
        return request.execute();
    }

    public List<MatchListDto> findByPuuid(String puuid, String region, int start, int count) {
        String endpoint = "lol/match/v5/matches/by-puuid/" + puuid + "/ids?start=" + start + "&count=" + count
                + "&api_key=";
        RiotRequest<List<MatchListDto>> request = new RiotRequest<>(Helper.getRiotApiRegion(region), endpoint, apiKey,
                new MatchListMapper());
        return request.execute();
    }
}
