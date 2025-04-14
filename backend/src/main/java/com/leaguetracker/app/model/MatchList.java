package com.leaguetracker.app.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "summoner_matchlist", indexes = {
        @Index(name = "idx_puuid", columnList = "puuid")}, uniqueConstraints = {
        @UniqueConstraint(name = "uk_puuid_match", columnNames = {"puuid", "match_id"})
})
public class MatchList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String puuid;
    private String matchId;
}
