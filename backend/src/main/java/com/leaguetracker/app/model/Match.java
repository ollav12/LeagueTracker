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
@Table(name = "match", indexes = {
        @Index(name = "idx_puuid", columnList = "puuid")}, uniqueConstraints = {
        @UniqueConstraint(name = "uk_puuid_match", columnNames = {"puuid", "match_id"})
})
public class Match {

    @Id
    @Column(name = "match_id")
    private String matchId;

    @Column(name = "puuid")
    private String puuid;
}
