package com.leaguetracker.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.leaguetracker.app.dto.AccountDto;
import com.leaguetracker.app.dto.SummonerDto;
import com.leaguetracker.app.dto.response.SummonerResponse;
import com.leaguetracker.app.mapper.SummonerMapper;
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
    public SummonerResponse getSummoner(String summonerName, String region, String tag) {
        AccountDto account = riotService.Account.findByRiotId(region, summonerName, tag);
        if (account != null) {
            SummonerDto summoner = riotService.Summoner.findByPuuid(account.puuid(), region);
            System.out.println("Sumoner: " + summoner);
            return SummonerMapper.toResponse(summonerName, tag, summoner);
        }

        return null;
    }

    public Summoner saveSummoner(Summoner summoner) {
        return summonerRepository.save(summoner);
    }

    public List<Summoner> getAllSummoners() {
        return summonerRepository.findAll();
    }
}
