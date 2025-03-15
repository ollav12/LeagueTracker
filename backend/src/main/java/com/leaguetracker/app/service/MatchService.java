package com.leaguetracker.app.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

//import com.fasterxml.jackson.databind.ObjectMapper;
import com.leaguetracker.app.config.EnvConfig;
import com.leaguetracker.app.model.SummonerMatch;
import com.leaguetracker.app.repository.MatchRepository;

@Service
public class MatchService {

    private final String apiKey;
    private final RestTemplate restTemplate = new RestTemplate();
    //private final ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(MatchService.class);

    @Autowired
    private MatchRepository matchRepository;

    public MatchService(EnvConfig envConfig) {
        this.apiKey = envConfig.getApiKey();
    }

    public SummonerMatch saveMatch(SummonerMatch match) {
        return matchRepository.save(match);
    }

    public List<SummonerMatch> getMatches() {
        return matchRepository.findAll();
    }

    public SummonerMatch getMatcheById(String matchId) {
        return matchRepository.findById(matchId).orElseThrow();
    }

    public List<SummonerMatch> getMatchesById(String matchId) { 
        return matchRepository.findAllByMatchId(matchId);
    }

    public List<SummonerMatch> getMatchesByPuuid(String puuid) {  
        return matchRepository.findAllByPuuid(puuid);
    }

    // Riot api calls
    public SummonerMatch get(){
        return null;
    }

    public List<SummonerMatch> fetchMatches(String puuid) {
        try {
            String region = "europe";
            int start = 0;
            int end = 20;
            String matchUrl = "https://" + region + ".api.riotgames.com/lol/match/v5/matches/by-puuid/" + puuid + "/ids?start=" + start + "&count=" + end + "&api_key=" + apiKey;
            
            logger.info("Fetching matches from URL: {}", matchUrl);
            
            @SuppressWarnings("rawtypes")
            ResponseEntity<List> response = restTemplate.getForEntity(matchUrl, List.class);

            if(response.getStatusCode().is2xxSuccessful()) {
                List<SummonerMatch> list = new ArrayList<>();
                @SuppressWarnings("unchecked")
                List<String> matches = response.getBody();
                if (matches != null) {
                    for (String match : matches) {
                        SummonerMatch summonerMatch = new SummonerMatch(match, puuid);
                        list.add(summonerMatch);
                        matchRepository.save(summonerMatch);
                    }
                } else {
                    logger.warn("No matches found for puuid: {}", puuid);
                }
                return list;
            } else {
                logger.error("Failed to fetch matches. Status code: {}", response.getStatusCode());
            }
      
        } catch (Exception e) {
            logger.error("Exception occurred while fetching matches: ", e);
        }

        return new ArrayList<>();
    }




}
