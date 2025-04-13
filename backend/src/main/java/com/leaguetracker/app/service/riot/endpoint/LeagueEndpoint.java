package com.leaguetracker.app.service.riot.endpoint;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;

import com.leaguetracker.app.config.WebClientConfig;
import com.leaguetracker.app.dto.response.RiotLeagueResponse;
import com.leaguetracker.app.mapper.RiotLeagueMapper;
import com.leaguetracker.app.service.riot.RiotRequest;

@Service
@RequiredArgsConstructor
public class LeagueEndpoint {

    private final WebClient webClient;
    private final WebClientConfig webClientConfig;

    public RiotLeagueResponse findByPuuid(String puuid, String region) {
        RiotRequest<RiotLeagueResponse> request = new RiotRequest<>(
                RiotEndpoint.LEAGUE_BY_PUUID,
                region,
                RiotLeagueMapper.INSTANCE::toRiotLeagueResponse,
                webClientConfig,
                webClient,
                puuid);
        return request.execute();
    }

    public RiotLeagueResponse findBySummonerId(String summonerId, String region) {
        RiotRequest<RiotLeagueResponse> request = new RiotRequest<>(
                RiotEndpoint.LEAGUE_BY_SUMMONER,
                region,
                RiotLeagueMapper.INSTANCE::toRiotLeagueResponse,
                webClientConfig,
                webClient,
                summonerId);
        return request.execute();
    }

}
