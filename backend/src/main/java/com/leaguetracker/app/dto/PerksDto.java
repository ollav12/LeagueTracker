package com.leaguetracker.app.dto;

public record PerksDto(
        PerkStatsDto statPerks,
        PerkStyleDto[] styles) {
}