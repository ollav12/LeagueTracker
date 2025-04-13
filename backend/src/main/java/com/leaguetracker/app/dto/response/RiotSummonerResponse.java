package com.leaguetracker.app.dto.response;

import lombok.Builder;

@Builder
public record RiotSummonerResponse(
        String id,
        String accountId,
        String puuid,
        int profileIconId,
        long revisionDate,
        int summonerLevel) {
}
