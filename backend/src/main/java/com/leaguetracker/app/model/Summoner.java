package com.leaguetracker.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "summoner")
public class Summoner {
 
    @Id
    private String puuid;
    private String summonerName;
    private String region;
    private int summonerProfileIconId;
    private long summonerLevel;

    public Summoner(String puuid, String summonerName, String region, int summonerProfileIconId, long summonerLevel) {
        this.puuid = puuid;
        this.summonerName = summonerName;
        this.region = region;
        this.summonerProfileIconId = summonerProfileIconId;
        this.summonerLevel = summonerLevel;
    }

    public Summoner() {
    }

    public String getPuuid() {
        return puuid;
    }
    public String getRegion() {
        return region;
    }

    public long getSummonerLevel() {
        return summonerLevel;
    }

    public String getSummonerName() {
        return summonerName;
    }

    public int getSummonerProfileIconId() {
        return summonerProfileIconId;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setSummonerLevel(long summonerLevel) {
        this.summonerLevel = summonerLevel;
    }

    public void setSummonerName(String summonerName) {
        this.summonerName = summonerName;
    }

    public void setSummonerProfileIconId(int summonerProfileIconId) {
        this.summonerProfileIconId = summonerProfileIconId;
    }
}
