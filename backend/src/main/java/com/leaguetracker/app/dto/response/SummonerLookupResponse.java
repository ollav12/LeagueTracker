package com.leaguetracker.app.dto.response;

import java.util.List;

import com.leaguetracker.app.dto.RiotLeagueEntry;

public record SummonerLookupResponse(
        String summonerName,
        String tagLine,
        String id,
        String accountId,
        String puuid,
        int profileIconId,
        long revisionDate,
        int summonerLevel,
        List<RiotLeagueEntry> ranked) {
}
