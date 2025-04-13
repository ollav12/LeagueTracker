package com.leaguetracker.app.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leaguetracker.app.dto.MatchDto;
import com.leaguetracker.app.dto.RiotMatchIdEntry;
import com.leaguetracker.app.dto.response.RiotMatchListResponse;
import com.leaguetracker.app.model.MatchList;
import com.leaguetracker.app.model.SummonerMatch;
import com.leaguetracker.app.repository.MatchListRepository;
import com.leaguetracker.app.repository.MatchRepository;
import com.leaguetracker.app.service.riot.RiotService;

import org.springframework.data.domain.PageRequest;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final MatchRepository matchRepository;
    private final MatchListRepository matchListRepository;
    private final RiotService riotService;

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

    /**
     * Get matches by matchIds, retrieving from database first when available
     * 
     * @param region   Region of the matches
     * @param matchIds List of match IDs to retrieve
     * @return List of match data
     */
    public List<MatchDto> getMatches(String region, List<String> matchIds) {
        List<MatchDto> matches = new ArrayList<>();

        for (String matchId : matchIds) {
            // Try to find match in local repository first
            SummonerMatch existingMatch = matchRepository.findByMatchId(matchId);

            if (existingMatch != null) {
                // Match exists in database, convert entity to DTO
                try {
                    // Create a JSON structure that maps to MatchDto
                    StringBuilder jsonBuilder = new StringBuilder();
                    jsonBuilder.append("{");
                    jsonBuilder.append("\"metadata\": ").append(existingMatch.getMetadataJson()).append(",");
                    jsonBuilder.append("\"info\": ").append(existingMatch.getInfoJson());
                    jsonBuilder.append("}");

                    // Convert the combined JSON to MatchDto
                    MatchDto matchDto = objectMapper.readValue(jsonBuilder.toString(), MatchDto.class);
                    matches.add(matchDto);
                    System.out.println("Retrieved match from database: " + matchId);
                } catch (Exception e) {
                    System.err.println("Error parsing match data from database: " + e.getMessage());
                    // If parsing fails, fetch from API as fallback
                    fetchAndSaveMatch(matchId, region, matches);
                }
            } else {
                // Match not in database, fetch from API and save
                fetchAndSaveMatch(matchId, region, matches);
            }
        }

        return matches;
    }

    /**
     * Helper method to fetch match from API and save to database
     */
    private void fetchAndSaveMatch(String matchId, String region, List<MatchDto> matches) {
        try {
            MatchDto matchDto = riotService.Match.findByMatchId(matchId, region);
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

    /**
     * Get ranks of players from given match
     * 
     * @return list of ranks
     */
    public List<SummonerMatch> getSummonersRanks(String matchId) {
        return null;
    }

    public void saveMatchList(List<RiotMatchIdEntry> matchList, String puuid) {
        for (RiotMatchIdEntry match : matchList) {
            try {
                if (!matchListRepository.existsByPuuidAndMatchId(puuid, match.matchId())) {
                    MatchList newMatch = MatchList.builder()
                            .puuid(puuid)
                            .matchId(match.matchId())
                            .build();
                    matchListRepository.save(newMatch);
                } else {
                    System.out.println("Match already exists for puuid: " + puuid + ", matchId: " + match.matchId());
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

    public enum MatchListMode {
        LIGHT,
    }

    public List<RiotMatchIdEntry> updateMatchList(String puuid, String region, MatchListMode mode) {
        List<MatchList> matchListTemp = matchListRepository.findByPuuid(puuid);
        List<RiotMatchIdEntry> matchList = new ArrayList<RiotMatchIdEntry>();
        for (MatchList match : matchListTemp) {
            matchList.add(new RiotMatchIdEntry(match.getPuuid(), match.getMatchId()));
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

    public void saveMatches(List<RiotMatchIdEntry> matchList, String puuid) {
        for (RiotMatchIdEntry match : matchList) {
            MatchList newMatch = MatchList.builder()
                    .puuid(puuid)
                    .matchId(match.matchId())
                    .build();
            matchListRepository.save(newMatch);
        }
    }

}
