package com.leaguetracker.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.leaguetracker.app.model.SummonerRank;

@Repository
public interface RankRepository extends JpaRepository<SummonerRank, String>{
   List<SummonerRank> findByPuuid(String puuid);
}
