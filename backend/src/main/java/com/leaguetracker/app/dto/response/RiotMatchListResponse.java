package com.leaguetracker.app.dto.response;

import java.util.List;

import com.leaguetracker.app.dto.RiotMatchIdEntry;

public record RiotMatchListResponse(
        List<RiotMatchIdEntry> matchIds) {
}
