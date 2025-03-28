package com.leaguetracker.app.service.riot.endpoint;

import org.springframework.stereotype.Service;

@Service
public class SummonerEndpoint {

    private final String apiKey;

    public SummonerEndpoint(String apiKey) {
        this.apiKey = apiKey;
    }
}
