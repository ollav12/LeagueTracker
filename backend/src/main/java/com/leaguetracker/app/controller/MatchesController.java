package com.leaguetracker.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.leaguetracker.app.dto.MatchDto;
import com.leaguetracker.app.dto.MatchListDto;
import com.leaguetracker.app.model.MatchList;
import com.leaguetracker.app.model.SummonerMatch;
import com.leaguetracker.app.repository.MatchListRepository;
import com.leaguetracker.app.service.MatchService;
import com.leaguetracker.app.service.MatchService.MatchListMode;
import com.leaguetracker.app.service.UpdateService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/matches")
public class MatchesController {

    private MatchService matchService;
    @Autowired
    private UpdateService updateService;

    @Autowired
    private MatchListRepository matchListRepository;

    public MatchesController(MatchService matchService) {
        this.matchService = matchService;
    }

    /**
     * Get a match by matchId
     * 
     * @param matchId
     * @return match
     */
    @GetMapping("/match/{matchId}/by-puuid/{puuid}/region/{region}")
    public ResponseEntity<MatchDto> getMatch(
            @PathVariable String matchId,
            @PathVariable String puuid,
            @PathVariable String region) {
        return ResponseEntity.ok(matchService.getMatch(matchId, region));
    }

    /**
     * Get matches from matchId
     * 
     * @return list of matches
     */
    @GetMapping()
    public ResponseEntity<MatchDto> getMatche(@RequestBody String matchId, @RequestBody String region) {
        return ResponseEntity.ok(matchService.getMatch(matchId, region));
    }

    /**
     * Get ranks of players from given match
     * 
     * @param puuid
     * @return list of ranks
     */
    @GetMapping("{matchId}/ranks")
    public ResponseEntity<List<SummonerMatch>> getSummonersRanks(@PathVariable String matchId) {
        return ResponseEntity.ok(matchService.getSummonersRanks(matchId));
    }

    @GetMapping("/matchlist")
    public ResponseEntity<List<MatchListDto>> updateMatchList(@RequestParam String puuid, @RequestParam String region,
            @RequestParam MatchListMode mode) {
        return ResponseEntity.ok(matchService.updateMatchList(puuid, region, mode));
    }

    @GetMapping("/list/{puuid}")
    public ResponseEntity<List<MatchList>> getMatches(@PathVariable String puuid) {
        return ResponseEntity.ok(matchListRepository.findByPuuid(puuid));
    }
}