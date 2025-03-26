package com.leaguetracker.app.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "summoner_matches")
public class SummonerMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String matchId;
    private String puuid;

    @Column(columnDefinition = "TEXT")
    private String metadataJson;

    @Column(columnDefinition = "TEXT")
    private String infoJson;

    public SummonerMatch() {
    }

    @Data
    public static class Metadata {
        private String dataVersion;
        private String matchId;
        private List<String> participants;
    }

    @Data
    public static class MatchInfo {
        private String endOfGameResult;
        private Long gameCreation;
        private Integer gameDuration;
        private Long gameEndTimestamp;
        private Long gameId;
        private String gameMode;
        private String gameName;
        private Long gameStartTimestamp;
        private String gameType;
        private String gameVersion;
        private Integer mapId;
        private List<Participant> participants;
    }

}
