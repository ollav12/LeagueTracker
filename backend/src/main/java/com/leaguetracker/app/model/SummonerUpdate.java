package com.leaguetracker.app.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "summoner_updates")
public class SummonerUpdate {

    @Id
    private String puuid;

    private LocalDateTime lastUpdatedSummoner;
    private LocalDateTime lastUpdatedRanks;
    private LocalDateTime lastUpdatedMatches;

    public SummonerUpdate() {
    }

    public SummonerUpdate(String puuid) {
        this.puuid = puuid;
    }
}
