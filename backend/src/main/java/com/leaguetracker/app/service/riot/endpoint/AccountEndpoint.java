package com.leaguetracker.app.service.riot.endpoint;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leaguetracker.app.config.WebClientConfig;
import com.leaguetracker.app.dto.response.RiotAccountResponse;
import com.leaguetracker.app.helper.Helper;
import com.leaguetracker.app.service.riot.RiotRequest;

@Service
@RequiredArgsConstructor
public class AccountEndpoint {

    private final WebClient webClient;
    private final WebClientConfig webClientConfig;
    private final ObjectMapper objectMapper;

    enum Account {
        findByRiotId,
        findByPuuid
    }

    public RiotAccountResponse findByRiotId(String region, String summonerName, String tag) {
        RiotRequest<RiotAccountResponse> request = new RiotRequest<>(
                RiotEndpoint.ACCOUNT_BY_RIOT_ID,
                Helper.getRiotApiRegion(region),
                objectMapper,
                RiotAccountResponse.class,
                webClientConfig,
                webClient,
                summonerName,
                tag);
        return request.execute();
    }

    public RiotAccountResponse findByPuuid(String puuid, String region) {
        RiotRequest<RiotAccountResponse> request = new RiotRequest<>(
                RiotEndpoint.ACCOUNT_BY_PUUID,
                Helper.getRiotApiRegion(region),
                objectMapper,
                RiotAccountResponse.class,
                webClientConfig,
                webClient,
                puuid);
        return request.execute();
    }
}
