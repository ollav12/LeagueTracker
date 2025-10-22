package com.leaguetracker.app.controller;

import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.leaguetracker.app.dto.response.RiotMatchResponse;
import com.leaguetracker.app.model.MatchDetails;
import com.leaguetracker.app.service.MatchService;
import com.leaguetracker.app.service.MatchService.MatchListMode;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/matches")
public class MatchesController {

    private final MatchService matchService;

    @GetMapping("/{matchId}/regions/{region}")
    public ResponseEntity<RiotMatchResponse> getMatch(
            @NonNull @PathVariable String matchId,
            @NonNull @PathVariable String region) {
        return ResponseEntity.ok(matchService.getMatch(matchId, region));
    }

    @GetMapping("/{matchId}/ranks")
    public ResponseEntity<List<MatchDetails>> getSummonersRanks(@NonNull @PathVariable String matchId) {
        return ResponseEntity.ok(matchService.getSummonersRanks(matchId));
    }

    @GetMapping("/matchlist")
    public ResponseEntity<List<String>> updateMatchList(
            @NonNull @RequestParam String puuid,
            @NonNull @RequestParam String region,
            @NonNull @RequestParam MatchListMode mode) {
        return ResponseEntity.ok(matchService.updateMatchList(puuid, region, mode));
    }
}