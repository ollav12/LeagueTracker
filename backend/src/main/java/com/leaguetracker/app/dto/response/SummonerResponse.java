package com.leaguetracker.app.dto.response;

public record SummonerResponse(
                String summonerName,
                String tagLine,
                String id,
                String accountId,
                String puuid,
                int profileIconId,
                long revisionDate,
                int summonerLevel) {
}
