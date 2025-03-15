package com.leaguetracker.app.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.leaguetracker.app.config.EnvConfig;
import com.leaguetracker.app.model.SummonerMatch;
import com.leaguetracker.app.repository.MatchRepository;
import com.leaguetracker.app.service.MatchService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/matches")
public class MatchController /*~~(Could not parse as Java)~~>*/{

    @Autowired
    private MatchService matchService;


    @GetMapping()
    public ResponseEntity<List<SummonerMatch>> getMatches() {
        return ResponseEntity.ok(matchService.getMatches());
    }

    @GetMapping("/match/{matchId}")
    public ResponseEntity<List<SummonerMatch>> getMatchId(@PathVariable String matchId) {
        return ResponseEntity.ok(matchService.getMatchesById(matchId));
    }
    
    @GetMapping("/puuid/{puuid}")
    public ResponseEntity<List<SummonerMatch>> getMatchesForUser(@PathVariable String puuid) {
        return ResponseEntity.ok(matchService.getMatchesByPuuid(puuid));
    }

    @PostMapping()
    public ResponseEntity<List<SummonerMatch>> refreshMatchesForUser(@RequestBody Map<String, String> req) {
        String puuid = req.get("puuid");
        return ResponseEntity.ok(matchService.fetchMatches(puuid));
    }
    
}
