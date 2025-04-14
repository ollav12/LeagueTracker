package com.leaguetracker.app.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    @Getter
    @Setter
    @ToString
    @Builder
    public static class Metadata {
        private String dataVersion;
        private String matchId;
        private List<String> participants;
    }

    @Getter
    @Setter
    @ToString
    @Builder
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

    @Getter
    @Setter
    @ToString
    @Builder
    public static class Participant {
        private String puuid;
        private String summonerId;
        private String summonerName;
        private Integer teamId;

        private Integer championId;
        private String championName;

        private Integer kills;
        private Integer deaths;
        private Integer assists;
        private Boolean win;
        private String teamPosition;
        private Integer totalDamageDealtToChampions;
        private Integer goldEarned;
        private Integer totalMinionsKilled;
        private Integer visionScore;

        private Integer allInPings;
        private Integer assistMePings;

        private Object challenges;
    }
}
