package com.leaguetracker.app.dto.response;

import java.util.List;

import com.leaguetracker.app.dto.RiotLeagueEntry;

import lombok.Builder;

@Builder
public record RiotLeagueResponse(
        List<RiotLeagueEntry> leagues) {

}
