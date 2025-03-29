package com.leaguetracker.app.dto;

public record ObjectivesDto(
        ObjectiveDto baron,
        ObjectiveDto champion,
        ObjectiveDto dragon,
        ObjectiveDto inhibitor,
        ObjectiveDto riftHerald,
        ObjectiveDto tower) {
}
