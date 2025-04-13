package com.leaguetracker.app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;

@JsonIgnoreProperties(ignoreUnknown = true)
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

        @JsonIgnoreProperties(ignoreUnknown = true)
        @Builder
        public static record MiniSeriesDto(
                        int losses,
                        String progress,
                        int target,
                        int wins) {
        }
}
