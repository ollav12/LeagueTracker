package com.leaguetracker.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "summoner_matchlist", indexes = {
        @Index(name = "idx_puuid", columnList = "puuid") }, uniqueConstraints = {
                @UniqueConstraint(name = "uk_puuid_match", columnNames = { "puuid", "match_id" })
        })
public class MatchList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String puuid;
    private String matchId;
}
