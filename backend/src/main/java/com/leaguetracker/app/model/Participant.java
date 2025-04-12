package com.leaguetracker.app.model;

import lombok.Data;

@Data
public class Participant {
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