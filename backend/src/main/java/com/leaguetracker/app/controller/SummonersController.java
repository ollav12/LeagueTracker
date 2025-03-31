package com.leaguetracker.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<List<MatchList>> getSummary(
            @RequestParam String puuid,
            @RequestParam String accountId,
            @RequestParam String region

    ) {
        try {
            List<MatchList> matchSummary = summonerService.getSummary(puuid, accountId,
                    region);
            return ResponseEntity.ok(matchSummary);
        } catch (Exception e) {
            return null;
        }
    }
}
