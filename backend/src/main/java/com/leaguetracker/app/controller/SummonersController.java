package com.leaguetracker.app.controller;

import java.util.List;
import java.util.Map;

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
import com.leaguetracker.app.service.SummonerService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/summoners")
public class SummonersController {

    private final SummonerService summonerService;

    @GetMapping("/{region}/{summonerName}-{tag}")
    public ResponseEntity<SummonerLookupResponse> getSummoner(@PathVariable String region,
            @PathVariable String summonerName,
            @PathVariable String tag) {

        SummonerLookupRequest request = new SummonerLookupRequest(region, summonerName, tag);
        SummonerLookupResponse summoner = summonerService.getSummonerDetails(request);
        return ResponseEntity.ok(summoner);

    }

    @GetMapping("/summary")
    public ResponseEntity<SummonerSummaryResponse> getSummary(@Valid SummonerSummaryRequest request) {
        return ResponseEntity.ok(summonerService.getSummary(request));
    }
}
