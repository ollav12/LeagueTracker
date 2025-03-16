package com.leaguetracker.app.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.leaguetracker.app.model.SummonerRank;
import com.leaguetracker.app.service.RankService;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/ranks")
public class RankController {

    
    @Autowired
    private RankService rankService;

    @GetMapping()
    public ResponseEntity<List<SummonerRank>> getRanks() {
        return ResponseEntity.ok(rankService.getRanks());
    }

    @GetMapping("/{puuid}")
    public ResponseEntity<List<SummonerRank>> getRankByPuuid(@PathVariable String puuid) {
        return ResponseEntity.ok(rankService.getRankByPuuid(puuid));
    }

    @PostMapping()
    public ResponseEntity<List<SummonerRank>> refreshRanksForUSer(@RequestBody Map<String, String> req) {
        String puuid = req.get("puuid");
        return ResponseEntity.ok(rankService.fetchRanks(puuid));
    }
}
