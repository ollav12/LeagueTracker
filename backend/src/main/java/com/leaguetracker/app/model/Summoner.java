package com.leaguetracker.app.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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

    public Summoner(
            String puuid,
            String summonerName,
            String region,
            int profileIconId,
            int summonerLevel,
            long revisionDate,
            String accountId,
            String id,
            String tagLine) {
        this.puuid = puuid;
        this.summonerName = summonerName;
        this.region = region;
        this.profileIconId = profileIconId;
        this.summonerLevel = summonerLevel;
        this.revisionDate = revisionDate;
        this.accountId = accountId;
        this.id = id;
        this.tagLine = tagLine;
        this.lastUpdated = new Date();
    }

    public Summoner() {
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public String getPuuid() {
        return puuid;
    }

    public String getRegion() {
        return region;
    }

    public int getSummonerLevel() {
        return summonerLevel;
    }

    public String getSummonerName() {
        return summonerName;
    }

    public int getProfileIconId() {
        return profileIconId;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getId() {
        return id;
    }

    public long getRevisionDate() {
        return revisionDate;
    }

    public String getTagLine() {
        return tagLine;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setSummonerLevel(int summonerLevel) {
        this.summonerLevel = summonerLevel;
    }

    public void setSummonerName(String summonerName) {
        this.summonerName = summonerName;
    }

    public void setProfileIconId(int summonerProfileIconId) {
        this.profileIconId = summonerProfileIconId;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = new Date();
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPuuid(String puuid) {
        this.puuid = puuid;
    }

    public void setRevisionDate(long revisionDate) {
        this.revisionDate = revisionDate;
    }

    public void setTagLine(String tagLine) {
        this.tagLine = tagLine;
    }
}
