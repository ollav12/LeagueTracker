package com.leaguetracker.app.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.leaguetracker.app.model.Summoner;
import com.leaguetracker.app.service.SummonerService;
import com.leaguetracker.app.service.UpdateService;
import com.leaguetracker.app.service.UpdateService.UpdateType;
import com.leaguetracker.app.service.riot.RiotService;

@RestController
@RequestMapping("/summoners")
public class SummonerController {

    @Autowired
    private SummonerService summonerService;
    @Autowired
    private UpdateService updateService;

    @Autowired
    private RiotService riotService;

    @GetMapping()
    public ResponseEntity<List<Summoner>> getAllSummoners() {
        return ResponseEntity.ok(summonerService.getAllSummoners());
    }

    /**
     * Get summoner info
     * 
     * @param region
     * @param summonerName
     * @param tag
     * @return
     */
    @GetMapping("/{region}/{summonerName}-{tag}")
    public ResponseEntity<?> getSummonerInfo(@PathVariable String region,
            @PathVariable String summonerName,
            @PathVariable String tag) {

        try {
            Object account = summonerService.getSummoner(summonerName, region, tag);
            if (account == null) {
                return null;
            }
        } finally {
        }

        try {
            // First check if we have this summoner in database
            Summoner existingSummoner = summonerService.getSummoner(summonerName, region, tag);

            if (existingSummoner != null) {
                String puuid = existingSummoner.getPuuid();

                // Check rate limiting using the updateService
                LocalDateTime lastUpdate = updateService.getLastUpdatedTime(puuid, UpdateType.SUMMONER);
                LocalDateTime now = LocalDateTime.now();

                // If last update was less than 5 minutes ago
                if (lastUpdate != null && ChronoUnit.SECONDS.between(lastUpdate, now) < 300) {
                    // Calculate remaining cooldown
                    long remainingSeconds = 300 - ChronoUnit.SECONDS.between(lastUpdate, now);

                    // Return the existing data with rate limit info
                    Map<String, Object> response = new HashMap<>();
                    response.put("summoner", existingSummoner);
                    response.put("cooldownSeconds", remainingSeconds);
                    response.put("isRateLimited", true);

                    return ResponseEntity.ok(response);
                }

                // If we're here, we can fetch fresh data
            }

            // Fetching fresh data from Riot API
            JsonNode accountData = riotService.Account.fetchAccountData(region, summonerName, tag);
            String puuid = accountData.path("puuid").asText();

            // Fetching Summoner Data
            JsonNode summonerData = riotService.fetchSummonerData(region + "1", puuid);

            // Create and save summoner
            Summoner summoner = new Summoner(
                    puuid,
                    summonerName,
                    region,
                    summonerData.path("profileIconId").asInt(),
                    summonerData.path("summonerLevel").asInt());

            // Save to database
            summonerService.saveSummoner(summoner);

            // Update the last updated time
            updateService.updateLastUpdatedTime(puuid, UpdateType.SUMMONER);

            return ResponseEntity.ok(summoner);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

}
