package com.leaguetracker.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "summoner_ranks")
public class SummonerRank {
    
    @Id
    private String puuid;
    private String queueType;
    private String rank;
    private String tier;
    private String lp;
    private int wins;
    private int losses;
    private boolean veteran;
    private boolean inactive;
    private boolean freshBlood;
    private boolean hotStreak;

    public SummonerRank(String puuid, String queueType, String rank, String tier, String lp, int wins, int losses, boolean veteran, boolean inactive, boolean freshBlood, boolean hotStreak) {
        this.puuid = puuid;
        this.queueType = queueType;
        this.rank = rank;
        this.tier = tier;
        this.lp = lp;
        this.wins = wins;
        this.losses = losses;
        this.veteran = veteran;
        this.inactive = inactive;
        this.freshBlood = freshBlood;
        this.hotStreak = hotStreak;
    }

    public SummonerRank() {
    }

    public String getPuuid() {
        return puuid;
    }
    public int getLosses() {
        return losses;
    }
    public String getLp() {
        return lp;
    }
    public String getQueueType() {
        return queueType;
    }
    public String getRank() {
        return rank;
    }
    public String getTier() {
        return tier;
    }
    public int getWins() {
        return wins;
    }
    public boolean getVeteran() {
        return this.veteran;
    }

    public boolean getInactive() {
        return this.inactive;
    }

    public boolean getFreshBlood() {
        return this.freshBlood;
    }

    public boolean getHotStreak() {
        return this.hotStreak;
    }

    public void setFreshBlood(boolean freshBlood) {
        this.freshBlood = freshBlood;
    }

    public void setHotStreak(boolean hotStreak) {
        this.hotStreak = hotStreak;
    }

    public void setInactive(boolean inactive) {
        this.inactive = inactive;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public void setLp(String lp) {
        this.lp = lp;
    }

    public void setQueueType(String queueType) {
        this.queueType = queueType;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public void setPuuid(String puuid) {
        this.puuid = puuid;
    }

    public void setVeteran(boolean veteran) {
        this.veteran = veteran;
    }
    
    public void setWins(int wins) {
        this.wins = wins;
    }
}
