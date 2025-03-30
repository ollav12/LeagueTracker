package com.leaguetracker.app.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "summoner_ranks")
public class SummonerRank {

    @Id
    private String leagueId;
    private String puuid;
    private String queueType;
    private String rank;
    private String summonerId;
    private String tier;
    private int leaguePoints;
    private int wins;
    private int losses;
    private boolean veteran;
    private boolean inactive;
    private boolean freshBlood;
    private boolean hotStreak;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "losses", column = @Column(name = "mini_series_losses")),
            @AttributeOverride(name = "wins", column = @Column(name = "mini_series_wins"))
    })
    private MiniSeries miniSeries;

    @Embeddable
    public static class MiniSeries {
        private int wins;
        private String progress;
        private int target;
        private int losses;

        public MiniSeries(int wins, String progress, int target, int losses) {
            this.wins = wins;
            this.progress = progress;
            this.target = target;
            this.losses = losses;
        }

        public int getLosses() {
            return losses;
        }

        public String getProgress() {
            return progress;
        }

        public int getTarget() {
            return target;
        }

        public int getWins() {
            return wins;
        }
    }

    public SummonerRank(
            String leagueId,
            String summonerId,
            String puuid,
            String queueType,
            String rank,
            String tier,
            int leaguePoints,
            int wins,
            int losses,
            boolean veteran,
            boolean inactive,
            boolean freshBlood,
            boolean hotStreak,
            SummonerRank.MiniSeries miniSeries) {
        this.leagueId = leagueId;
        this.summonerId = summonerId;
        this.puuid = puuid;
        this.queueType = queueType;
        this.rank = rank;
        this.tier = tier;
        this.leaguePoints = leaguePoints;
        this.wins = wins;
        this.losses = losses;
        this.veteran = veteran;
        this.inactive = inactive;
        this.freshBlood = freshBlood;
        this.hotStreak = hotStreak;
        this.miniSeries = (miniSeries != null) ? miniSeries : new MiniSeries(0, "", 0, 0); // Default values
    }

    public SummonerRank() {
    }

    public String getPuuid() {
        return puuid;
    }

    public int getLosses() {
        return losses;
    }

    public int getLeaguePoints() {
        return leaguePoints;
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

    public String getLeagueId() {
        return leagueId;
    }

    public String getSummonerId() {
        return summonerId;
    }

    public MiniSeries getMiniSeries() {
        if (miniSeries == null) {
            return new MiniSeries(0, "", 0, 0); // Default empty MiniSeries if null
        }
        return miniSeries;
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

    public void setLeaguePoints(int lp) {
        this.leaguePoints = lp;
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

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }

    public void setSummonerId(String summonerId) {
        this.summonerId = summonerId;
    }

    public void setMiniSeries(MiniSeries miniSeries) {
        this.miniSeries = miniSeries;
    }
}
