package com.leaguetracker.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leaguetracker.app.model.SummonerMatch;
import com.leaguetracker.app.service.MatchService;

import jakarta.websocket.server.PathParam;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/matches")
public class MatchController {

    @Autowired
    private MatchService matchService;
    
    @GetMapping()
    public ResponseEntity<List<SummonerMatch>> getMatches() {
        return ResponseEntity.ok(matchService.getMatches());
    }

    @GetMapping("/{matchId}")
    public ResponseEntity<List<String>> getMatchId(@PathVariable String matchId) {
        return ResponseEntity.ok(matchService.getMatchesById(matchId));
    }
    
    
}
