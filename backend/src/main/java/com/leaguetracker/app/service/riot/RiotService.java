package com.leaguetracker.app.service.riot;

import com.leaguetracker.app.config.EnvConfig;
import com.leaguetracker.app.service.riot.endpoint.AccountEndpoint;
import com.leaguetracker.app.service.riot.endpoint.LeagueEndpoint;
import com.leaguetracker.app.service.riot.endpoint.SummonerEndpoint;

import org.springframework.stereotype.Service;

//import RiotRateLimiter

@Service
public class RiotService {

    private final String apiKey;

    public final AccountEndpoint Account;
    public final SummonerEndpoint Summoner;
    public final LeagueEndpoint League;
    // private final RiotRateLimiter limter;

    public RiotService(EnvConfig envConfig) {
        this.apiKey = envConfig.getApiKey();

        this.Account = new AccountEndpoint(apiKey);
        this.Summoner = new SummonerEndpoint(apiKey);
        this.League = new LeagueEndpoint(apiKey);
    }
}
