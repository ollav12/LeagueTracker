package com.leaguetracker.app.service.riot.endpoint;

import org.springframework.stereotype.Service;

import com.leaguetracker.app.dto.response.RiotAccountResponse;
import com.leaguetracker.app.helper.Helper;
import com.leaguetracker.app.mapper.RiotAccountMapper;
import com.leaguetracker.app.service.riot.RiotRequest;

@Service
public class AccountEndpoint {

    private final String apiKey;

    public AccountEndpoint(String apiKey) {
        this.apiKey = apiKey;
    }

    public RiotAccountResponse findByRiotId(String region, String summonerName, String tag) {
        String endpoint = "riot/account/v1/accounts/by-riot-id/" + summonerName + "/" + tag.toUpperCase() + "?api_key=";
        RiotRequest<RiotAccountResponse> request = new RiotRequest<>(
                Helper.getRiotApiRegion(region),
                endpoint,
                apiKey,
                RiotAccountMapper.INSTANCE::toRiotAccountResponse);
        return request.execute();
    }

    public RiotAccountResponse findByPuuid(String puuid, String region) {
        String endpoint = "lol/summoner/v4/summoners/by-puuid/" + puuid;
        RiotRequest<RiotAccountResponse> request = new RiotRequest<>(
                Helper.getRiotApiRegion(region),
                endpoint,
                apiKey,
                RiotAccountMapper.INSTANCE::toRiotAccountResponse);
        return request.execute();
    }
}
