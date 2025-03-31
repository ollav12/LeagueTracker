package com.leaguetracker.app.service.riot.endpoint;

import org.springframework.stereotype.Service;

import com.leaguetracker.app.dto.SummonerDto;
import com.leaguetracker.app.mapper.SummonerMapper;
import com.leaguetracker.app.service.riot.RiotRequest;

@Service
public class SummonerEndpoint {

    private final String apiKey;

    public SummonerEndpoint(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Fetch summoner by puuid
     * 
     * @param puuid
     * @return
     */
    public SummonerDto findByPuuid(String puuid, String region) {
        String endpoint = "lol/summoner/v4/summoners/by-puuid/" + puuid + "?api_key=";
        RiotRequest<SummonerDto> request = new RiotRequest<>(region, endpoint, apiKey,
                new SummonerMapper());
        return request.execute();
    }
}
