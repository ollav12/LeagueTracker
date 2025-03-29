package com.leaguetracker.app.service.riot.endpoint;

import org.springframework.stereotype.Service;

import com.leaguetracker.app.dto.AccountDto;
import com.leaguetracker.app.helper.Helper;
import com.leaguetracker.app.mapper.AccountMapper;
import com.leaguetracker.app.service.riot.RiotRequest;

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
    public AccountDto findByRiotId(String region, String summonerName, String tag) {
        String endpoint = "riot/account/v1/accounts/by-riot-id/" + summonerName + "/" + tag.toUpperCase();
        RiotRequest<AccountDto> request = new RiotRequest<>(Helper.getRiotApiRegion(region), endpoint, apiKey,
                new AccountMapper());
        return request.execute();
    }

    /**
     * Fetch user by puuid
     * 
     * @param puuid
     * @return
     */
    public AccountDto findByPuuid(String puuid, String region) {
        String endpoint = "lol/summoner/v4/summoners/by-puuid/" + puuid;
        RiotRequest<AccountDto> request = new RiotRequest<>(Helper.getRiotApiRegion(region), endpoint, apiKey,
                new AccountMapper());
        return request.execute();
    }

    /**
     * Fetch porifle icon
     * 
     * @param summonerProfileIconId
     * @return
     */
    public String fetchProfileIcon(int summonerProfileIconId) {
        // Fetching Summoner Iccon url from the profileIconId
        return "https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/v1/profile-icons/"
                + summonerProfileIconId + ".jpg";
    }
}
