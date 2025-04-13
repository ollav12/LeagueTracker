package com.leaguetracker.app.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.leaguetracker.app.dto.response.RiotLeagueResponse.RiotLeagueEntry;

import lombok.Builder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
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
