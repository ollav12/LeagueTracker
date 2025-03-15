package com.leaguetracker.app.service;
import com.leaguetracker.app.config.EnvConfig;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RiotApiService {
 
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String apiKey;

    public RiotApiService(EnvConfig envConfig) {
        this.apiKey = envConfig.getApiKey();
    }

    public JsonNode fetchAccountData(String region, String summonerName, String tag) {
        try {
            region = getRegionFromTag(tag);
            String accountUrl = "https://" + region + ".api.riotgames.com/riot/account/v1/accounts/by-riot-id/"
                    + summonerName + "/" + tag + "?api_key=" + apiKey;
            String accountResponse = restTemplate.getForObject(accountUrl, String.class);
            JsonNode accountData;
            accountData = objectMapper.readTree(accountResponse);
            return accountData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public JsonNode fetchSummonerData(String tag, String puuid) {
        try {
            String summonerUrl = "https://" + tag + ".api.riotgames.com/lol/summoner/v4/summoners/by-puuid/"
                    + puuid + "?api_key=" + apiKey;
            String summonerResponse = restTemplate.getForObject(summonerUrl, String.class);
            JsonNode summonerData = objectMapper.readTree(summonerResponse);
            return summonerData;
        } catch (Exception e) {
            return null;
        }

    }

    public String fetchProfileIcon(int summonerProfileIconId) {
        // Fetching Summoner Iccon url from the profileIconId
        return "https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/v1/profile-icons/"
                + summonerProfileIconId + ".jpg";
    }


    public String getRegionFromTag(String tag) {
        switch (tag) {
            case "EUW":
                return "Europe";
            case "NA":
                return "Americas";
            case "EUNE":
                return "Europe";
            default:
                return "Europe";
        }
    }
}
