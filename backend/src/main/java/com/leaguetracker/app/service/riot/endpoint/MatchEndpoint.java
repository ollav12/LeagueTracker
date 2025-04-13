package com.leaguetracker.app.service.riot.endpoint;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leaguetracker.app.config.WebClientConfig;
import com.leaguetracker.app.dto.MatchDto;
import com.leaguetracker.app.dto.response.RiotMatchListResponse;
import com.leaguetracker.app.helper.Helper;
import com.leaguetracker.app.service.riot.RiotRequest;

@Service
@RequiredArgsConstructor
public class MatchEndpoint {

    private final WebClient webClient;
    private final WebClientConfig webClientConfig;
    private final ObjectMapper objectMapper;

    public MatchDto findByMatchId(String matchId, String region) {
        RiotRequest<MatchDto> request = new RiotRequest<>(
                RiotEndpoint.MATCH_BY_ID,
                Helper.getRiotApiRegion(region),
                objectMapper,
                MatchDto.class,
                webClientConfig,
                webClient,
                matchId);
        return request.execute();
    }

    public RiotMatchListResponse findByPuuid(String puuid, String region, int start, int count) {
        RiotRequest<RiotMatchListResponse> request = new RiotRequest<>(
                RiotEndpoint.MATCH_LIST_BY_PUUID,
                Helper.getRiotApiRegion(region),
                objectMapper,
                RiotMatchListResponse.class,
                webClientConfig,
                webClient,
                puuid,
                start,
                count);
        return request.execute();
    }
}
