package com.leaguetracker.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leaguetracker.app.model.Summoner;
import com.leaguetracker.app.repository.SummonerRepository;
import com.leaguetracker.app.service.riot.RiotService;

@Service
public class SummonerService {

    private final SummonerRepository summonerRepository;

    private final RiotService riotService;

    public SummonerService(SummonerRepository summonerRepository, RiotService riotService) {
        this.summonerRepository = summonerRepository;
        this.riotService = riotService;
    }

    /**
     * Get a summoner
     * 
     * @param puuid
     * @return
     */
    public Summoner getSummoner(String summonerName, String region, String tag) {
        Object account = riotService.Account.getAccountData(region, summonerName, tag);
        System.out.println("Account: ");
        System.out.println(account);

        return summonerRepository.findById("te").orElse(null);
    }

    public Summoner saveSummoner(Summoner summoner) {
        return summonerRepository.save(summoner);
    }

    public List<Summoner> getAllSummoners() {
        return summonerRepository.findAll();
    }
}
