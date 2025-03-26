package com.leaguetracker.app.model;

import lombok.Data;

@Data
public class Participant {
    // Basic player info
    private String puuid;
    private String summonerId;
    private String summonerName;
    private Integer teamId;

    // Champion info
    private Integer championId;
    private String championName;

    // Performance stats
    private Integer kills;
    private Integer deaths;
    private Integer assists;
    private Boolean win;
    private String teamPosition;
    private Integer totalDamageDealtToChampions;
    private Integer goldEarned;
    private Integer totalMinionsKilled;
    private Integer visionScore;

    // Ping data
    private Integer allInPings;
    private Integer assistMePings;

    // You can add more fields as needed
    // For rarely accessed fields, consider using a Map
    private Object challenges; // Store as Object for flexibility
}