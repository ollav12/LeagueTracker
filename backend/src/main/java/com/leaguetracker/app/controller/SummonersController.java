package com.leaguetracker.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.leaguetracker.app.dto.request.SummonerLookupRequest;
import com.leaguetracker.app.dto.request.SummonerSummaryRequest;
import com.leaguetracker.app.dto.response.SummonerLookupResponse;
import com.leaguetracker.app.dto.response.SummonerSummaryResponse;
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

    @GetMapping("/summary")
    public ResponseEntity<SummonerSummaryResponse> getSummary(@Valid SummonerSummaryRequest request) {
        return ResponseEntity.ok(summonerInfoService.getSummary(request));
    }
}
