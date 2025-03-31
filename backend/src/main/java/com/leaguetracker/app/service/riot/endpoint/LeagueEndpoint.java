package com.leaguetracker.app.service.riot.endpoint;

import java.util.List;

import org.springframework.stereotype.Service;

import com.leaguetracker.app.dto.LeagueDto;
import com.leaguetracker.app.mapper.LeagueMapper;
import com.leaguetracker.app.service.riot.RiotRequest;

@Service
public class LeagueEndpoint {

    private final String apiKey;

    public LeagueEndpoint(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Fetch user by summoner id
     * 
     * @param summonerId
     * @param region
     * @return
     */
    public List<LeagueDto> findBySummonerId(String summonerId, String region) {
        String endpoint = "lol/league/v4/entries/by-summoner/" + summonerId + "?api_key=";
        RiotRequest<List<LeagueDto>> request = new RiotRequest<>(region, endpoint, apiKey,
                LeagueMapper::toDtoList);
        System.out.println("TEST");
        return request.execute();
    }

}
