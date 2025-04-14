package com.leaguetracker.app.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.leaguetracker.app.dto.response.RiotSummonerResponse;
import com.leaguetracker.app.mapper.RiotSummonerMapper;
import com.leaguetracker.app.model.Summoner;
import com.leaguetracker.app.repository.SummonerRepository;
import com.leaguetracker.app.service.riot.RiotService;

@Slf4j
@Service
@RequiredArgsConstructor
public class SummonerService {
    private final SummonerRepository summonerRepository;
    private final RiotService riotService;

    public RiotSummonerResponse getSummoner(String puuid, String name, String tag) {
        Summoner summoner = summonerRepository.findById(puuid).orElse(null);

        if (summoner != null) {
            System.out.println("Retrieved summoner from database: " + summoner.getSummonerName());
            return RiotSummonerMapper.INSTANCE.toRiotSummonerResponse(summoner);
        }
        System.out.println("Summoner not found in database, fetching from Riot API");
        RiotSummonerResponse riotSummoner = riotService.Summoner.findByPuuid(puuid, tag);

        if (riotSummoner != null) {
            Summoner newSummoner = RiotSummonerMapper.INSTANCE.toSummoner(riotSummoner);
            summonerRepository.save(newSummoner);
            System.out.println("Fetched summoner from Riot API and saved to database: " + name);
        }
        return riotSummoner;
    }

    public Summoner saveSummoner(Summoner summoner) {
        return summonerRepository.save(summoner);
    }
}
