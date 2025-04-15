package com.leaguetracker.app.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.leaguetracker.app.dto.response.RiotLeagueResponse.RiotLeagueEntry;

import com.leaguetracker.app.model.Rank;
import lombok.Builder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public record SummonerLookupResponse(
        String puuid,
        int profileIconId,
        int summonerLevel,
        List<Rank> ranked) {
}
