package com.leaguetracker.app.dto.response;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.leaguetracker.app.dto.MatchDto;

import lombok.Builder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public record SummonerSummaryResponse(
                List<String> matchIds,
                List<MatchDto> matchDetails,
                Map<String, Object> stats) {
}