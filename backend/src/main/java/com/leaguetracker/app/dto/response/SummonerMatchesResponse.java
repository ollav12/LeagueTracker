package com.leaguetracker.app.dto.response;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public record SummonerMatchesResponse(
        List<String> matchIds,
        List<RiotMatchResponse> matchDetails,
        Map<String, Object> stats) {
}