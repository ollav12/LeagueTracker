package com.leaguetracker.app.dto;

public record MetadataDto(
                String dataVersion,
                String matchId,
                String[] participants) {
}
