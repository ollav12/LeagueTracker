package com.leaguetracker.app.controller;

import com.leaguetracker.app.dto.request.SummonerUpdateRequest;
import com.leaguetracker.app.dto.response.SummonerUpdateResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.leaguetracker.app.dto.request.SummonerLookupRequest;
import com.leaguetracker.app.dto.request.SummonerMatchesRequest;
import com.leaguetracker.app.dto.response.SummonerLookupResponse;
import com.leaguetracker.app.dto.response.SummonerMatchesResponse;
import com.leaguetracker.app.service.SummonerInfoService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/summoners")
public class SummonersController {

    private final SummonerInfoService summonerInfoService;

    @GetMapping("/{region}/{summonerName}-{tag}")
    public ResponseEntity<SummonerLookupResponse> getSummoner(
            @PathVariable String region,
            @PathVariable String summonerName,
            @PathVariable String tag) {

        SummonerLookupRequest request = SummonerLookupRequest.builder()
                .region(region)
                .summonerName(summonerName)
                .tag(tag)
                .build();
        SummonerLookupResponse summoner = summonerInfoService.getSummonerDetails(request);
        return ResponseEntity.ok(summoner);
    }

    @GetMapping("/{puuid}/match-history")
    public ResponseEntity<SummonerMatchesResponse> getMatchHistory(@Valid SummonerMatchesRequest request) {
        return ResponseEntity.ok(summonerInfoService.getMatchHistory(request));
    }

    @GetMapping("/{puuid}/match-history/load-more")
    public ResponseEntity<SummonerMatchesResponse> loadMoreMatches(
            @PathVariable String puuid,
            @Valid SummonerMatchesRequest request) {
        return ResponseEntity.ok(summonerInfoService.loadMoreMatches(request));
    }

    @GetMapping("/{puuid}/update")
    public ResponseEntity<SummonerUpdateResponse> updateSummoner(@PathVariable String puuid, @Valid SummonerUpdateRequest request) {
        return ResponseEntity.ok(summonerInfoService.updateSummoner(request));
    }
}
