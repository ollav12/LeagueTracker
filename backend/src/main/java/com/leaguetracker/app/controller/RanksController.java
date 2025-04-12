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
import com.leaguetracker.app.model.SummonerRank;
import com.leaguetracker.app.service.RankService;
import com.leaguetracker.app.service.UpdateService;
import com.leaguetracker.app.service.UpdateService.UpdateType;

@RestController
@RequestMapping("/api/v1/ranks")
public class RanksController {

    @Autowired
    private RankService rankService;
    @Autowired
    private UpdateService updateService;

    @GetMapping()
    public ResponseEntity<List<SummonerRank>> getRanks() {
        return ResponseEntity.ok(rankService.getRanks());
    }

    @GetMapping("/{puuid}")
    public ResponseEntity<?> getRankByPuuid(@PathVariable String puuid) {
        // Get existing ranks from database
        List<SummonerRank> existingRanks = rankService.getRankByPuuid(puuid);

        // Check rate limiting
        LocalDateTime lastUpdate = updateService.getLastUpdatedTime(puuid, UpdateType.RANKS);
        LocalDateTime now = LocalDateTime.now();

        // If we have existing data and last update was less than 5 minutes ago
        if (lastUpdate != null && ChronoUnit.SECONDS.between(lastUpdate, now) < 300) {
            // Calculate remaining cooldown
            long remainingSeconds = 300 - ChronoUnit.SECONDS.between(lastUpdate, now);

            // Return existing data with rate limit info
            Map<String, Object> response = new HashMap<>();
            response.put("ranks", existingRanks);
            response.put("cooldownSeconds", remainingSeconds);
            response.put("isRateLimited", true);
            response.put("message", "Using cached data. Refresh available in " + remainingSeconds + " seconds");

            return ResponseEntity.ok(response);
        }

        // If no rate limiting applies, fetch fresh data
        try {
            List<SummonerRank> freshRanks = rankService.fetchRanks(puuid);

            // Update the last updated time
            updateService.updateLastUpdatedTime(puuid, UpdateType.RANKS);

            // Return fresh data
            Map<String, Object> response = new HashMap<>();
            response.put("ranks", freshRanks);
            response.put("isRateLimited", false);
            response.put("message", "Data refreshed successfully");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // If fetch fails, return existing data with an error message
            Map<String, Object> response = new HashMap<>();
            response.put("ranks", existingRanks);
            response.put("isRateLimited", false);
            response.put("error", "Failed to refresh data: " + e.getMessage());

            return ResponseEntity.ok(response);
        }
    }
}
