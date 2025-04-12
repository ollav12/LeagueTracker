package com.leaguetracker.app.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "summoner_updates")
public class SummonerUpdate {

    @Id
    private String puuid;

    private LocalDateTime lastUpdatedSummoner;
    private LocalDateTime lastUpdatedRanks;
    private LocalDateTime lastUpdatedMatches;
}
