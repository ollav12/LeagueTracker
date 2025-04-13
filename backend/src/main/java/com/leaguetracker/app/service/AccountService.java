package com.leaguetracker.app.service;

import com.leaguetracker.app.dto.response.RiotAccountResponse;
import com.leaguetracker.app.service.riot.RiotService;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AccountService {
    private final RiotService riotService;

    public AccountService(RiotService riotService) {
        this.riotService = riotService;
    }

    @Cacheable(value = "accounts", key = "'account:' + #region + ':' + #summonerName + ':' + #tag")
    public RiotAccountResponse getAccount(String region, String summonerName, String tag) {
        log.info("Cache MISS - Calling Riot API for \" + region + \":\" + summonerName + \"#\" + tag");
        return riotService.Account.findByRiotId(region, summonerName, tag);
    }
}
