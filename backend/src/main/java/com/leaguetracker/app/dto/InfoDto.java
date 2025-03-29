package com.leaguetracker.app.dto;

import java.util.List;
import java.util.Optional;

public record InfoDto(
                long gameCreation,
                long gameDuration,
                long gameId,
                String gameMode,
                String gameName,
                long gameStartTimestamp,
                String gameType,
                String gameVersion,
                int mapId,
                List<ParticipantDto> participants,
                String platformId,
                int queueId,
                List<TeamDto> teams,
                Optional<String> tournamentCode) {
}