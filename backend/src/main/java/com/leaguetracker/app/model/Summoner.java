package com.leaguetracker.app.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "summoner")
@EntityListeners(AuditingEntityListener.class)
public class Summoner {

    @Id
    private String puuid;
    private String summonerName;
    private String region;
    private int profileIconId;
    private int summonerLevel;
    private long revisionDate;
    private String accountId;
    private String id;
    private String tagLine;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;
}