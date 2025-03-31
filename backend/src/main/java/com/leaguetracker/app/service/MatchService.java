package com.leaguetracker.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leaguetracker.app.config.EnvConfig;
import com.leaguetracker.app.dto.MatchDto;
import com.leaguetracker.app.dto.MatchListDto;
import com.leaguetracker.app.model.MatchList;
import com.leaguetracker.app.model.SummonerMatch;
import com.leaguetracker.app.repository.MatchListRepository;
import com.leaguetracker.app.repository.MatchRepository;
import com.leaguetracker.app.service.riot.RiotService;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import org.springframework.data.domain.PageRequest;

@Service
public class MatchService {

    private final String apiKey;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(MatchService.class);

    private final MatchRepository matchRepository;
    private final MatchListRepository matchListRepository;
    private final RiotService riotService;

    public MatchService(MatchRepository matchRepository, EnvConfig envConfig, RiotService riotService,
            MatchListRepository matchListRepository) {
        this.matchRepository = matchRepository;
        this.riotService = riotService;
        this.matchListRepository = matchListRepository;
        this.apiKey = envConfig.getApiKey();
    }

    /**
     * Get a match by matchId
     * 
     * @param matchId
     * @param region
     * @return match
     */
    public MatchDto getMatch(String matchId, String region) {
        return riotService.Match.findByMatchId(matchId, region);
    }

    public List<String> getNextMatchIds(String puuid, String lastMatchId, int limit) {
        if (puuid == null) {
            System.out.println("PUUID cannot be null");
            return new ArrayList<>();
        }

        try {
            // Add count check before query
            long matchCount = matchListRepository.count();
            System.out.println("Total matches in database: " + matchCount);

            if (lastMatchId != null) {
                System.out.println(
                        "Fetching matches for puuid: " + puuid + ", lastMatchId: " + lastMatchId + ", limit: " + limit);
                List<String> matchIds = matchListRepository.getNextMatchIds(puuid, lastMatchId,
                        PageRequest.of(0, limit));
                System.out.println("Found " + matchIds.size() + " matches after lastMatchId: " + lastMatchId);
                return matchIds;
            } else {
                System.out.println("Initial load for puuid: " + puuid + ", limit: " + limit);
                List<String> matchIds = matchListRepository.findAllByPuuidOrderByMatchIdDesc(puuid, limit);
                System.out.println("Initial load found " + matchIds.size() + " matches");
                return matchIds;
            }
        } catch (Exception e) {
            System.out.println("Error fetching next match IDs for puuid: " + puuid);
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<MatchDto> getMatches(String region, List<String> matchIds) {
        return matchIds.stream()
                .map(matchId -> riotService.Match.findByMatchId(matchId, region))
                .collect(Collectors.toList());
    }

    /**
     * Get ranks of players from given match
     * 
     * @return list of ranks
     */
    public List<SummonerMatch> getSummonersRanks(String matchId) {
        return null;
    }

    public void saveMatchList(List<MatchListDto> matchList, String puuid) {
        if (matchList == null || matchList.isEmpty()) {
            logger.debug("No matches to save for puuid: {}", puuid);
            return;
        }

        try {
            List<MatchList> entities = matchList.stream()
                    .filter(dto -> dto != null && dto.matchId() != null)
                    .map(dto -> {
                        MatchList entity = new MatchList(dto.puuid(), dto.matchId());
                        return entity;
                    })
                    .collect(Collectors.toList());

            if (!entities.isEmpty()) {
                matchListRepository.saveAll(entities);
                logger.info("Saved {} matches for puuid: {}", entities.size(), puuid);
            } else {
                logger.warn("No valid matches to save after filtering for puuid: {}", puuid);
            }
        } catch (Exception e) {
            logger.error("Error saving match list for puuid: {}", puuid, e);
            throw new RuntimeException("Failed to save match list", e);
        }
    }

    public List<MatchList> getMatchListByPuuid(String puuid) {
        System.out.println("Looking for matches with puuid: {}" + puuid);
        List<MatchList> matches = matchListRepository.findByPuuid(puuid);
        System.out.println("Found {} matches" + matches.size());
        return matches;
    }

    public enum MatchListMode {
        LIGHT,
    }

    public List<MatchListDto> updateMatchList(String puuid, String region, MatchListMode mode) {
        List<MatchList> matchListTemp = matchListRepository.findByPuuid(puuid);
        List<MatchListDto> matchList = new ArrayList<MatchListDto>();
        for (MatchList match : matchListTemp) {
            matchList.add(new MatchListDto(match.getPuuid(), match.getMatchid()));
        }

        int start = 0;
        int count = 100;
        boolean shouldContinueFetching = true;

        while (shouldContinueFetching) {
            List<MatchListDto> newMatchList = riotService.Match.findByPuuid(puuid, region, start, count);
            System.out.println("FETCHES MATCHES");
            System.out.println(newMatchList);
            // Proper null and empty checks for List
            if (newMatchList == null || newMatchList.isEmpty()) {
                saveMatches(matchList, puuid);
                return matchList;
            }

            // Use addAll instead of spread operator (which is not available in Java)
            matchList.addAll(newMatchList);

            // Update start index for next batch
            start += count;

            // Add logic to determine if we should continue fetching
            if (mode == MatchListMode.LIGHT) {
                shouldContinueFetching = false;
            }
        }
        System.out.println("SAVING MATCHES");
        saveMatches(matchList, puuid);
        return matchList;
    }

    public void saveMatches(List<MatchListDto> matchList, String puuid) {
        for (MatchListDto match : matchList) {
            MatchList newMatch = new MatchList(puuid, match.matchId());
            matchListRepository.save(newMatch);
        }
    }

}
