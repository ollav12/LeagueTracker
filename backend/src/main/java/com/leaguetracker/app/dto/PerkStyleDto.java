package com.leaguetracker.app.dto;

import java.util.List;

public record PerkStyleDto(String description, List<PerkStyleSelectionDto> selections, int style) {
}