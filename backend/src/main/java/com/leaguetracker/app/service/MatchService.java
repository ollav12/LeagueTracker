package com.leaguetracker.app.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.ObjectMapper;
import com.leaguetracker.app.config.EnvConfig;
import com.leaguetracker.app.model.SummonerMatch;
import com.leaguetracker.app.model.SummonerRank;
import com.leaguetracker.app.model.SummonerUpdate;
import com.leaguetracker.app.repository.MatchRepository;
import com.leaguetracker.app.repository.SummonerUpdateRepository;

@Service
public class MatchService {

    private final String apiKey;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(MatchService.class);

    @Autowired
    private MatchRepository matchRepository;

    public MatchService(EnvConfig envConfig) {
        this.apiKey = envConfig.getApiKey();
    }

    public SummonerMatch saveMatch(Map<String, Object> matchData, String puuid) {
        SummonerMatch match = new SummonerMatch();

        // Extract the matchId from metadata
        Map<String, Object> metadata = (Map<String, Object>) matchData.get("metadata");
        String matchId = (String) metadata.get("matchId");

        // Set the fields for easy querying
        match.setMatchId(matchId);
        match.setPuuid(puuid);

        // Set the JSON data
        try {
            match.setMetadataJson(objectMapper.writeValueAsString(metadata));
            match.setInfoJson(objectMapper.writeValueAsString(matchData.get("info")));
        } catch (Exception e) {
            throw new RuntimeException("Error serializing match data", e);
        }

        return matchRepository.save(match);
    }

    public List<SummonerMatch> getMatches() {
        return matchRepository.findAll();
    }

    public SummonerMatch getMatchMatchId(String matchId) {
        return matchRepository.findByMatchId(matchId);
    }

    public List<SummonerMatch> getMatchesByPuuid(String puuid) {
        return matchRepository.findByPuuidInJson(puuid);
    }

    // Riot api calls
    public SummonerMatch get() {
        return null;
    }

    public List<SummonerMatch> fetchMatches(String puuid) {
        try {
            String region = "europe";
            int start = 0;
            int end = 20;
            String matchUrl = "https://" + region + ".api.riotgames.com/lol/match/v5/matches/by-puuid/" + puuid
                    + "/ids?start=" + start + "&count=" + end + "&api_key=" + apiKey;

            logger.info("Fetching matches from URL: {}", matchUrl);

            @SuppressWarnings("rawtypes")
            ResponseEntity<List> response = restTemplate.getForEntity(matchUrl, List.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                List<SummonerMatch> list = new ArrayList<>();
                @SuppressWarnings("unchecked")
                List<String> matches = response.getBody();
                if (matches != null) {
                    for (String match : matches) {

                        String matchDataUrl = "https://" + region + ".api.riotgames.com/lol/match/v5/matches/" + match
                                + "?api_key=" + apiKey;
                        @SuppressWarnings("rawtypes")
                        ResponseEntity<Map> matchDataResponse = restTemplate.getForEntity(matchDataUrl,
                                Map.class);

                        Map<String, Object> matchData = matchDataResponse.getBody();
                        SummonerMatch summonerMatch = saveMatch(matchData, puuid);
                        list.add(summonerMatch);
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
