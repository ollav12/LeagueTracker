package com.leaguetracker.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.leaguetracker.app.model.SummonerMatch;
import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<SummonerMatch, Long> {
    SummonerMatch findByMatchId(String matchId);

    List<SummonerMatch> findByPuuid(String puuid);
}
