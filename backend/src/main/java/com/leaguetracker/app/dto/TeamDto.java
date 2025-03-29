package com.leaguetracker.app.dto;

public record TeamDto(
        BanDto[] bans,
        ObjectivesDto objectives,
        int teamid,
        boolean win) {
}
