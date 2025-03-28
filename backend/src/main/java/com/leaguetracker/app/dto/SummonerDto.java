package com.leaguetracker.app.dto;

public record SummonerDto(String id, String accountId, String puuid, int profieIconId, long revisionDate,
        int summonerLevel) {
}
