package com.leaguetracker.app.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leaguetracker.app.model.Summoner;
import com.leaguetracker.app.service.SummonerService;

@RestController
@RequestMapping("/summoners")
public class SummonerController {

    private final SummonerService summonerService;

    public SummonerController(SummonerService summonerService) {
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
     * @return
     */
    @GetMapping("/{region}/{summonerName}-{tag}")
    public ResponseEntity<?> getSummoner(@PathVariable String region,
            @PathVariable String summonerName,
            @PathVariable String tag) {

        try {
            Object account = summonerService.getSummoner(summonerName, region, tag);
            if (account == null) {
                return null;
            } else {
                return ResponseEntity.ok(account);
            }
        } finally {
        }
    }

}
