package com.leaguetracker.app.service.riot.endpoint;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.leaguetracker.app.service.riot.RiotRequest;

import reactor.core.publisher.Mono;

@Service
public class AccountEndpoint {

    private final String apiKey;

    public AccountEndpoint(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Fetch user by riot ID
     * 
     * @param region
     * @param summonerName
     * @param tag
     * @return
     */
    public Mono<String> findByRiotID(String region, String summonerName, String tag) {
        String endpoint = "riot/account/v1/accounts/by-riot-id/" + summonerName + "/" + tag;
        RiotRequest request = new RiotRequest(region, endpoint, apiKey);
        return request.executeAsync();
    }

    public JsonNode getAccountData(String region, String summonerName, String tag) {
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
