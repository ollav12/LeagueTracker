package com.leaguetracker.app.service;

import com.leaguetracker.app.dto.AccountDto;
import com.leaguetracker.app.service.riot.RiotService;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final RiotService riotService;

    public AccountService(RiotService riotService) {
        this.riotService = riotService;
    }

    @Cacheable(value = "accounts", key = "'account:' + #region + ':' + #summonerName + ':' + #tag")
    public AccountDto getAccount(String region, String summonerName, String tag) {
        System.out.println("🔍 Cache MISS - Calling Riot API for " + region + ":" + summonerName + "#" + tag);
        AccountDto result = riotService.Account.findByRiotId(region, summonerName, tag);
        System.out.println("📦 API Result: " + result);
        return result;
    }
}
