package com.leaguetracker.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.leaguetracker.app.model.SummonerMatch;
import java.util.List;


@Repository
public interface MatchRepository extends JpaRepository<SummonerMatch, String>{
   
    List<SummonerMatch> findAllByMatchId(String matchId);
    List<SummonerMatch> findAllByPuuid(String puuid);
}
