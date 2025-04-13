package com.leaguetracker.app.service.riot.endpoint;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;

import com.leaguetracker.app.config.WebClientConfig;
import com.leaguetracker.app.dto.response.RiotSummonerResponse;
import com.leaguetracker.app.mapper.RiotSummonerMapper;
import com.leaguetracker.app.service.riot.RiotRequest;

@Service
@RequiredArgsConstructor
public class SummonerEndpoint {

    private final WebClient webClient;
    private final WebClientConfig webClientConfig;

    public RiotSummonerResponse findByPuuid(String puuid, String region) {
        RiotRequest<RiotSummonerResponse> request = new RiotRequest<>(
                RiotEndpoint.SUMMONER_BY_PUUID,
                region,
                RiotSummonerMapper.INSTANCE::toSummonerDto,
                webClientConfig,
                webClient,
                puuid);
        return request.execute();
    }
}
