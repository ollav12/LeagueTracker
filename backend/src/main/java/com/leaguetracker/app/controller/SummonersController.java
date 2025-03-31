package com.leaguetracker.app.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leaguetracker.app.dto.response.SummonerResponse;
import com.leaguetracker.app.model.MatchList;
import com.leaguetracker.app.model.Summoner;
import com.leaguetracker.app.repository.MatchListRepository;
import com.leaguetracker.app.service.SummonerService;

@RestController
@RequestMapping("/summoners")
public class SummonersController {

    private final SummonerService summonerService;
    @Autowired
    private MatchListRepository matchListRepository;

    private static final Logger logger = LoggerFactory.getLogger(SummonersController.class);

    public SummonersController(SummonerService summonerService) {
        this.summonerService = summonerService;
    }

    @GetMapping()
    public ResponseEntity<List<Summoner>> getAllSummoners() {
        return ResponseEntity.ok(summonerService.getAllSummoners());
    }

    /**
     * Get a summoner
     * 
     * @param region
     * @param summonerName
     * @param tag
     * @return Summoner response
     */
    @GetMapping("/{region}/{summonerName}-{tag}")
    public ResponseEntity<SummonerResponse> getSummoner(@PathVariable String region,
            @PathVariable String summonerName,
            @PathVariable String tag) {

        try {
            SummonerResponse summoner = summonerService.getSummonerDetails(summonerName, region, tag);
            return ResponseEntity.ok(summoner);
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getSummary(
            @RequestParam String puuid,
            @RequestParam String region,
            @RequestParam(required = false) String lastMatchId,
            @RequestParam(defaultValue = "20") int limit) {
        try {
            Map<String, Object> summary = summonerService.getSummary(puuid, region, lastMatchId, limit);
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            logger.error("Error fetching summary for puuid: {}", puuid, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
