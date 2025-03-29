package com.leaguetracker.app.service;

import com.leaguetracker.app.repository.RankRepository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leaguetracker.app.config.EnvConfig;
import com.leaguetracker.app.dto.LeagueDto;
import com.leaguetracker.app.model.SummonerRank;

@Service
public class RankService {

    private final EnvConfig envConfig;

    @Autowired
    private RankRepository rankRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String apiKey;

    public RankService(EnvConfig envConfig) {
        this.apiKey = envConfig.getApiKey();
        this.envConfig = envConfig;
    }

    public void saveLeagueDto(List<LeagueDto> ranks) {
        for (LeagueDto rank : ranks) {
            SummonerRank sumRank = new SummonerRank(
                    rank.leagueId(),
                    rank.summonerId(),
                    rank.puuid(),
                    rank.queueType(),
                    rank.rank(),
                    rank.tier(),
                    rank.leaguePoints(),
                    rank.wins(),
                    rank.losses(),
                    rank.veteran(),
                    rank.inactive(),
                    rank.freshBlood(),
                    rank.hotStreak());
            rankRepository.save(sumRank);
        }
    }

    public SummonerRank saveRank(SummonerRank rank) {
        return rankRepository.save(rank);
    }

    public List<SummonerRank> getRanks() {
        return rankRepository.findAll();
    }

    public List<SummonerRank> getRankByPuuid(String puuid) {
        return rankRepository.findByPuuid(puuid);
    }

    // Riot api calls
    public List<SummonerRank> fetchRanks(String puuid) {
        try {
            String region = "euw1";
            String rankUrl = "https://" + region + ".api.riotgames.com/lol/league/v4/entries/by-puuid/" + puuid
                    + "?api_key=" + apiKey;
            ResponseEntity<String> response = restTemplate.getForEntity(rankUrl, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                List<SummonerRank> list = new ArrayList<>();
                String responseBody = response.getBody();
                if (responseBody != null) {
                    List<SummonerRank> ranks = objectMapper.readValue(responseBody,
                            new TypeReference<List<SummonerRank>>() {
                            });
                    for (SummonerRank rank : ranks) {
                        rankRepository.save(rank);
                        list.add(rank);
                    }
                }
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
