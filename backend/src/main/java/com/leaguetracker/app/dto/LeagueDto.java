package com.leaguetracker.app.dto;

public record LeagueDto(
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

        public static record MiniSeriesDto(
                        int losses,
                        String progress,
                        int target,
                        int wins) {

        }
}
