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
     * @return summoner response
     */
    public SummonerResponse getSummoner(String summonerName, String region, String tag) {
        AccountDto account = riotService.Account.findByRiotId(region, summonerName, tag);
        System.out.println("Fetched account form riot api: " + account);
        if (account != null) {
            Summoner summoner = summonerRepository.findById(account.puuid()).orElse(null);
            if (summoner != null) {
                System.out.println("Retrieved summoner from database: " + summoner);
                return SummonerMapper.toResponse(summonerName, tag, new SummonerDto(
                        summoner.getId(),
                        summoner.getAccountId(),
                        summoner.getPuuid(),
                        summoner.getProfileIconId(),
                        summoner.getRevisionDate(),
                        summoner.getSummonerLevel()));
            }
            SummonerDto summonerDto = riotService.Summoner.findByPuuid(account.puuid(), region);
            System.out.println("Fetched summoner from riot api: " + summonerDto);
            SummonerResponse response = SummonerMapper.toResponse(summonerName, tag, summonerDto);
            Summoner newSummoner = SummonerMapper.toEntity(region, response);
            summonerRepository.save(newSummoner);
            System.out.println("Saved new summoner " + newSummoner);
            return response;
        }

        return null;
    }

    /**
     * Get account by puuid
     * 
     * @return AccountDto
     */
    public AccountDto getAccount(String puuid, String region) {
        return riotService.Account.findByPuuid(puuid, region);
    }

    public Summoner saveSummoner(Summoner summoner) {
        return summonerRepository.save(summoner);
    }

    public List<Summoner> getAllSummoners() {
        return summonerRepository.findAll();
    }
}
