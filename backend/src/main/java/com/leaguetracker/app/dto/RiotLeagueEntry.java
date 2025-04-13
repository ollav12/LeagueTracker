package com.leaguetracker.app.dto;

import lombok.Builder;

@Builder
public record RiotLeagueEntry(
                String leagueId,
                String queueType,
                String tier,
                String rank,
                String summonerId,
                String puuid,
                int leaguePoints,
                int wins,
                int losses,
                boolean veteran,
                boolean inactive,
                boolean freshBlood,
                boolean hotStreak,
                MiniSeriesDto miniSeries) {

        @Builder
        public static record MiniSeriesDto(
                        int losses,
                        String progress,
                        int target,
                        int wins) {
        }
}
