package com.leaguetracker.app.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.leaguetracker.app.model.SummonerMatch;
import com.leaguetracker.app.service.MatchService;
import com.leaguetracker.app.service.UpdateService;
import com.leaguetracker.app.service.UpdateService.UpdateType;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/matches")
public class MatchesController /* ~~(Could not parse as Java)~~> */ {

    @Autowired
    private MatchService matchService;
    @Autowired
    private UpdateService updateService;

    @GetMapping()
    public ResponseEntity<List<SummonerMatch>> getMatches() {
        return ResponseEntity.ok(matchService.getMatches());
    }

    @GetMapping("/match/{matchId}")
    public ResponseEntity<SummonerMatch> getMatchId(@PathVariable String matchId) {
        return ResponseEntity.ok(matchService.getMatchMatchId(matchId));
    }

    @GetMapping("/puuid/{puuid}")
    public ResponseEntity<List<SummonerMatch>> getMatchesForUser(@PathVariable String puuid) {
        return ResponseEntity.ok(matchService.getMatchesByPuuid(puuid));
    }

    @PostMapping()
    public ResponseEntity<?> refreshMatchesForUser(@RequestBody Map<String, String> req) {
        String puuid = req.get("puuid");

        // First, get existing matches from database
        List<SummonerMatch> existingMatches = matchService.getMatchesByPuuid(puuid);

        // Check when this summoner was last updated
        LocalDateTime lastUpdate = updateService.getLastUpdatedTime(puuid, UpdateType.MATCHES);
        LocalDateTime now = LocalDateTime.now();

        // If last update was less than 5 minutes ago
        if (lastUpdate != null && ChronoUnit.SECONDS.between(lastUpdate, now) < 300) {
            // Calculate remaining cooldown
            long remainingSeconds = 300 - ChronoUnit.SECONDS.between(lastUpdate, now);

            // Return existing data with rate limit info
            Map<String, Object> response = new HashMap<>();
            response.put("matches", existingMatches);
            response.put("cooldownSeconds", remainingSeconds);
            response.put("isRateLimited", true);
            response.put("message", "Using cached data. Refresh available in " + remainingSeconds + " seconds");

            return ResponseEntity.ok(response);
        }

        // No rate limiting needed, fetch from Riot API
        try {
            List<SummonerMatch> freshMatches = matchService.fetchMatches(puuid);

            // Update last update time
            updateService.updateLastUpdatedTime(puuid, UpdateType.MATCHES);

            // Return fresh data
            Map<String, Object> response = new HashMap<>();
            response.put("matches", freshMatches);
            response.put("isRateLimited", false);
            response.put("message", "Data refreshed successfully");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // If fetch fails, return existing data with an error message
            Map<String, Object> response = new HashMap<>();
            response.put("matches", existingMatches);
            response.put("isRateLimited", false);
            response.put("error", "Failed to refresh data: " + e.getMessage());

            return ResponseEntity.ok(response);
        }
    }
}