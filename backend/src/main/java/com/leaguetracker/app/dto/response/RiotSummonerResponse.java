package com.leaguetracker.app.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public record RiotSummonerResponse(
                String id,
                String accountId,
                String puuid,
                int profileIconId,
                long revisionDate,
                int summonerLevel) {
}
