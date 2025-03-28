package com.leaguetracker.app.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.leaguetracker.app.dto.AccountDto;
import com.leaguetracker.app.model.Summoner;
import com.leaguetracker.app.repository.SummonerRepository;
import com.leaguetracker.app.service.UpdateService.UpdateType;
import com.leaguetracker.app.service.riot.RiotService;

import reactor.core.publisher.Mono;

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
    public AccountDto getSummoner(String summonerName, String region, String tag) {
        AccountDto account = riotService.Account.findByRiotId(region, summonerName, tag);

        System.out.println("Account: ");
        System.out.println(account);

        // try {
        // First check if we have this summoner in database
        // Summoner existingSummoner = summonerService.getSummoner(summonerName, region,
        // tag);

        // if (existingSummoner != null) {
        // String puuid = existingSummoner.getPuuid();

        // Check rate limiting using the updateService
        // LocalDateTime lastUpdate = updateService.getLastUpdatedTime(puuid,
        // UpdateType.SUMMONER);
        // LocalDateTime now = LocalDateTime.now();

        // If last update was less than 5 minutes ago
        // if (lastUpdate != null && ChronoUnit.SECONDS.between(lastUpdate, now) < 300)
        // {
        // Calculate remaining cooldown
        // long remainingSeconds = 300 - ChronoUnit.SECONDS.between(lastUpdate, now);

        // Return the existing data with rate limit info
        // Map<String, Object> response = new HashMap<>();
        // response.put("summoner", existingSummoner);
        // response.put("cooldownSeconds", remainingSeconds);
        // response.put("isRateLimited", true);

        // return ResponseEntity.ok(response);
        // }

        // If we're here, we can fetch fresh data
        // }

        // Fetching fresh data from Riot API
        // Mono<String> accountData = riotService.Account.findByRiotId(region,
        // summonerName, tag);
        // String puuid = accountData.path("puuid").asText();

        // Fetching Summoner Data
        // Int summonerData = riotService.Account.fetchProfileIcon(region + "1", puuid);

        // Create and save summoner
        // Summoner summoner = new Summoner(
        // puuid,
        // summonerName,
        // region,
        // summonerData.path("profileIconId").asInt(),
        // summonerData.path("summonerLevel").asInt());

        // Save to database
        // summonerService.saveSummoner(summoner);

        // Update the last updated time
        // updateService.updateLastUpdatedTime(puuid, UpdateType.SUMMONER);

        // return ResponseEntity.ok(summoner);
        // } catch (Exception e) {
        // e.printStackTrace();
        // return ResponseEntity.status(500).body(null);
        // }

        return account;
    }

    public Summoner saveSummoner(Summoner summoner) {
        return summonerRepository.save(summoner);
    }

    public List<Summoner> getAllSummoners() {
        return summonerRepository.findAll();
    }
}
