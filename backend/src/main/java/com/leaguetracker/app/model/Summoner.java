package com.leaguetracker.app.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "summoner")
public class Summoner {

    @Id
    private String puuid;
    private String summonerName;
    private String region;
    private int profileIconId;
    private int summonerLevel;
    private long revisionDate;
    private String accountId;
    private String id;
    private String tagLine;
    private Date lastUpdated;
}
