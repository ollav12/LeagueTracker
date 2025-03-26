package com.leaguetracker.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.leaguetracker.app.model.SummonerUpdate;

@Repository
public interface SummonerUpdateRepository extends JpaRepository<SummonerUpdate, String> {

}
