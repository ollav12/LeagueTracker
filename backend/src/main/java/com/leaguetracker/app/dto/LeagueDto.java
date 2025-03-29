package com.leaguetracker.app.dto;

public record LeagueDto(
        String leagueId,
        String queueType,
        String tier,
        String rank,
        String summonerId,
        String puuid,
        String leaguePoints,
        String wins,
        String losses,
        boolean veteran,
        boolean inactive,
        boolean freshBlood,
        boolean hotStreak) {

}
