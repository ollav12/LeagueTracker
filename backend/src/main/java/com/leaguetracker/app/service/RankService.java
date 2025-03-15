package com.leaguetracker.app.service;
import com.leaguetracker.app.repository.RankRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leaguetracker.app.config.EnvConfig;
import com.leaguetracker.app.model.SummonerRank;

@Service
public class RankService {

    @Autowired
    private RankRepository rankRepository;
    
    private final String apiKey;

    public RankService(EnvConfig envConfig) {
        this.apiKey = envConfig.getApiKey();
    }

    public SummonerRank saveRank(SummonerRank rank) {
        return rankRepository.save(rank);
    }

    public List<SummonerRank> getRanks() {
        return rankRepository.findAll();
    }

    public List<SummonerRank> getRankByPuuid(String puuid) {
       return rankRepository.findByPuuid(puuid);
    }
}
