package com.leaguetracker.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leaguetracker.app.model.Summoner;
import com.leaguetracker.app.repository.SummonerRepository;

@Service
public class SummonerService {
    
    @Autowired
    private SummonerRepository summonerRepository;

    public Summoner saveSummoner(Summoner summoner) {
        return summonerRepository.save(summoner);
    }

    public Summoner getSummoner(String puuid) {
        return summonerRepository.findById(puuid).orElseThrow();
    }

    public List<Summoner> getAllSummoners() {
        return summonerRepository.findAll();
    }
}
