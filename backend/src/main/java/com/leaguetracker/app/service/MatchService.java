package com.leaguetracker.app.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leaguetracker.app.dto.response.RiotMatchListResponse;
import com.leaguetracker.app.dto.response.RiotMatchResponse;
import com.leaguetracker.app.mapper.RiotMatchMapper;
import com.leaguetracker.app.model.MatchList;
import com.leaguetracker.app.model.SummonerMatch;
import com.leaguetracker.app.repository.MatchListRepository;
import com.leaguetracker.app.repository.MatchRepository;
import com.leaguetracker.app.service.riot.RiotService;

import org.springframework.data.domain.PageRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final MatchRepository matchRepository;
    private final MatchListRepository matchListRepository;
    private final RiotService riotService;

    public enum MatchListMode {
        LIGHT,
    }

    public RiotMatchResponse getMatch(String matchId, String region) {
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

    public List<RiotMatchResponse> getMatches(String region, List<String> matchIds) {
        List<RiotMatchResponse> matches = new ArrayList<>();

        for (String matchId : matchIds) {
            SummonerMatch existingMatch = matchRepository.findByMatchId(matchId);

            if (existingMatch != null) {

                RiotMatchResponse response = RiotMatchMapper.INSTANCE.toRiotMatchResponse(existingMatch);

                matches.add(response);
                System.out.println("Retrieved match from database: " + matchId);
            } else {
                fetchAndSaveMatch(matchId, region, matches);
            }
        }
        return matches;
    }

    /**
     * Helper method to fetch match from API and save to database
     */
    private void fetchAndSaveMatch(String matchId, String region, List<RiotMatchResponse> matches) {
        try {
            RiotMatchResponse matchDto = riotService.Match.findByMatchId(matchId, region);
            if (matchDto != null) {
                matches.add(matchDto);

                // Save to database for future requests
                SummonerMatch matchEntity = new SummonerMatch();
                matchEntity.setMatchId(matchId);

                // Extract metadata and info separately
                String metadataJson = objectMapper.writeValueAsString(matchDto.metadata());
                String infoJson = objectMapper.writeValueAsString(matchDto.info());

                matchEntity.setMetadataJson(metadataJson);
                matchEntity.setInfoJson(infoJson);

                matchRepository.save(matchEntity);
                System.out.println("Fetched and saved match to database: " + matchId);
            }
        } catch (Exception e) {
            System.err.println("Error fetching match from API: " + matchId + ", " + e.getMessage());
        }
    }

    public List<SummonerMatch> getSummonersRanks(String matchId) {
        return null;
    }

    public void saveMatchList(List<String> matchList, String puuid) {
        for (String match : matchList) {
            try {
                if (!matchListRepository.existsByPuuidAndMatchId(puuid, match)) {
                    MatchList newMatch = MatchList.builder()
                            .puuid(puuid)
                            .matchId(match)
                            .build();
                    matchListRepository.save(newMatch);
                } else {
                    System.out.println("Match already exists for puuid: " + puuid + ", matchId: " + match);
                }
            } catch (Exception e) {
                System.err.println("Error saving match: " + e.getMessage());
            }
        }
    }

    public List<MatchList> getMatchListByPuuid(String puuid) {
        System.out.println("Looking for matches with puuid: {}" + puuid);
        List<MatchList> matches = matchListRepository.findByPuuid(puuid);
        System.out.println("Found {} matches" + matches.size());
        return matches;
    }

    public List<String> updateMatchList(String puuid, String region, MatchListMode mode) {
        List<MatchList> matchListTemp = matchListRepository.findByPuuid(puuid);
        List<String> matchList = new ArrayList<String>();
        for (MatchList match : matchListTemp) {
            matchList.add(match.getMatchId());
        }

        int start = 0;
        int count = 100;
        boolean shouldContinueFetching = true;

        while (shouldContinueFetching) {
            RiotMatchListResponse newMatchList = riotService.Match.findByPuuid(puuid, region, start, count);
            System.out.println("FETCHES MATCHES");
            // Proper null and empty checks for List
            if (newMatchList == null || newMatchList.matchIds().isEmpty()) {
                saveMatchList(matchList, puuid);
                return matchList;
            }

            // Use addAll instead of spread operator (which is not available in Java)
            matchList.addAll(newMatchList.matchIds());

            // Update start index for next batch
            start += count;

            // Add logic to determine if we should continue fetching
            if (mode == MatchListMode.LIGHT) {
                shouldContinueFetching = false;
            }
        }
        System.out.println("SAVING MATCHES");
        saveMatchList(matchList, puuid);
        return matchList;
    }

    public void saveMatches(List<String> matchList, String puuid) {
        for (String match : matchList) {
            MatchList newMatch = MatchList.builder()
                    .puuid(puuid)
                    .matchId(match)
                    .build();
            matchListRepository.save(newMatch);
        }
    }

}
