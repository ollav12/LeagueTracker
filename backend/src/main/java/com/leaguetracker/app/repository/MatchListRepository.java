package com.leaguetracker.app.repository;

import com.leaguetracker.app.model.MatchList;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchListRepository extends JpaRepository<MatchList, Long> {
    MatchList findByMatchId(String matchId);

    List<MatchList> findByPuuid(String puuid);

    MatchList findById(int id);
}
