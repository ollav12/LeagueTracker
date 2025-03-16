package com.leaguetracker.app.controller;

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
import com.leaguetracker.app.service.RiotApiService;
import com.leaguetracker.app.service.SummonerService;


@RestController
@RequestMapping("/summoners")
public class SummonerController {
    
    @Autowired
    private SummonerService summonerService;

    @Autowired
    private RiotApiService riotService;

    @GetMapping()
    public ResponseEntity<List<Summoner>> getAllSummoners() {
        return ResponseEntity.ok(summonerService.getAllSummoners());
    }
    


    @GetMapping("/{region}/{summonerName}-{tag}")
    public ResponseEntity<Map<String, Object>> getSummonerInfo(@PathVariable String region,
            @PathVariable String summonerName,
            @PathVariable String tag) {
        try {

            // Fetching Account Data
            JsonNode accountData = riotService.fetchAccountData(region, summonerName, tag);
            // Fetching Summoner Data
            JsonNode summonerData = riotService.fetchSummonerData(tag + "1",
                    accountData.path("puuid").asText());

            Summoner summoner = new Summoner(accountData.path("puuid").asText(), summonerName, region,
            summonerData.path("profileIconId").asInt(), summonerData.path("summonerLevel").asInt());

            // Save the summoner to the database
            summonerService.saveSummoner(summoner);

            // Fetching Summoner Icon
            String profileIconData = riotService.fetchProfileIcon(summoner.getSummonerProfileIconId());
            
            // Prepare the combined data to return
            Map<String, Object> combinedData = new HashMap<>();
            combinedData.put("accountData", accountData); // Full JSON data from the Riot API
            combinedData.put("summonerData", summonerData); // Return the PUUID separately if needed
            combinedData.put("summonerIconUrl", profileIconData); // Return the PUUID separately if needed

            return ResponseEntity.ok(combinedData);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Error retrieving summoner info"));
        }
    }


}
