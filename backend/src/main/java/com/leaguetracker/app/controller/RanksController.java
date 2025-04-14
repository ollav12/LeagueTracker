package com.leaguetracker.app.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.leaguetracker.app.dto.response.RiotLeagueResponse;
import com.leaguetracker.app.model.SummonerRank;
import com.leaguetracker.app.service.RankService;
import com.leaguetracker.app.service.UpdateService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ranks")
public class RanksController {

    private final RankService rankService;
    private final UpdateService updateService;

    @GetMapping
    public ResponseEntity<List<SummonerRank>> getRanks() {
        return ResponseEntity.ok(rankService.getRanks());
    }

    @GetMapping("/{puuid}/region/{region}")
    public ResponseEntity<?> getRankByPuuid(@PathVariable String puuid, @PathVariable String region) {
        //List<SummonerRank> existingRanks = rankService.getRankByPuuid(puuid);
        return ResponseEntity.ok(rankService.fetchSummonerLeague(puuid, region));


    }
}
