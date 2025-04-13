package com.leaguetracker.app.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
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
            @AttributeOverride(name = "wins", column = @Column(name = "mini_series_wins")),
            @AttributeOverride(name = "progress", column = @Column(name = "mini_series_progress")),
            @AttributeOverride(name = "target", column = @Column(name = "mini_series_target")),
    })
    private MiniSeries miniSeries;

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class MiniSeries {
        private int wins;
        private String progress;
        private int target;
        private int losses;
    }

}
