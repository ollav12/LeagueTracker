package com.leaguetracker.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.leaguetracker.app.LeaguetrackerApplication;
import com.leaguetracker.app.model.SummonerMatch;
import com.leaguetracker.app.repository.MatchRepository;

@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    public SummonerMatch saveMatch(SummonerMatch match) {
        return matchRepository.save(match);
    }

    public List<SummonerMatch> getMatches() {
            return matchRepository.findAll();
    }

    public List<String> getMatchesById(String matchId) {
        //return matchRepository.findAllById(matchId);
        return null;
    }

}
