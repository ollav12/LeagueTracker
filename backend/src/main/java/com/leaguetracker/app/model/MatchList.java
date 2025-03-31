package com.leaguetracker.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "summoner_matchlist", indexes = {
        @Index(name = "idx_puuid", columnList = "puuid")
})
public class MatchList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String puuid;
    @Column(unique = true)
    private String matchId;

    public MatchList(String puuid, String matchId) {
        this.puuid = puuid;
        this.matchId = matchId;
    }

    public MatchList() {

    }

    public String getPuuid() {
        return puuid;
    }

    public String getMatchid() {
        return matchId;
    }

    public void setPuuid(String puuid) {
        this.puuid = puuid;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

}
