package com.leaguetracker.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "summoner_matches")
public class SummonerMatch {
    
    @Id
    private String matchId;
    private String puuid;


    public SummonerMatch(String matchId, String puuid) {
        this.matchId = matchId;
        this.puuid = puuid;
    }

    public String getMatchId() {
        return matchId;
    }

    public String getPuuid() {
        return puuid;
    }

}
