package com.leaguetracker.app.service.riot.endpoint;

import java.util.List;

import org.springframework.stereotype.Service;

import com.leaguetracker.app.dto.RiotLeagueEntry;
import com.leaguetracker.app.dto.response.RiotLeagueResponse;
import com.leaguetracker.app.mapper.RiotLeagueMapper;
import com.leaguetracker.app.service.riot.RiotRequest;

@Service
public class LeagueEndpoint {

    private final String apiKey;

    public LeagueEndpoint(String apiKey) {
        this.apiKey = apiKey;
    }

    public RiotLeagueResponse findByPuuid(String puuid, String region) {
        String endpoint = "lol/league/v4/entries/by-puuid/" + puuid + "?api_key=";
        RiotRequest<RiotLeagueResponse> request = new RiotRequest<>(
                region,
                endpoint,
                apiKey,
                RiotLeagueMapper.INSTANCE::toRiotLeagueResponse);
        return request.execute();
    }

    public RiotLeagueResponse findBySummonerId(String summonerId, String region) {
        String endpoint = "lol/league/v4/entries/by-summoner/" + summonerId + "?api_key=";
        RiotRequest<RiotLeagueResponse> request = new RiotRequest<>(
                region,
                endpoint,
                apiKey,
                RiotLeagueMapper.INSTANCE::toRiotLeagueResponse);
        return request.execute();
    }

}
