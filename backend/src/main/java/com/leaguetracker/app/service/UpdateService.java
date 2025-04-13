package com.leaguetracker.app.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.leaguetracker.app.model.SummonerUpdate;
import com.leaguetracker.app.repository.SummonerUpdateRepository;

@Service
@RequiredArgsConstructor
public class UpdateService {

    private SummonerUpdateRepository summonerUpdateRepository;

    public enum UpdateType {
        SUMMONER, RANKS, MATCHES
    }

    public LocalDateTime getLastUpdatedTime(String puuid, UpdateType type) {
        SummonerUpdate update = summonerUpdateRepository.findById(puuid).orElse(null);
        if (update == null)
            return null;

        switch (type) {
            case SUMMONER:
                return update.getLastUpdatedSummoner();
            case RANKS:
                return update.getLastUpdatedRanks();
            case MATCHES:
                return update.getLastUpdatedMatches();
            default:
                return null;
        }
    }

    public void updateLastUpdatedTime(String puuid, UpdateType type) {
        SummonerUpdate update = summonerUpdateRepository.findById(puuid)
                .orElse(SummonerUpdate.builder().puuid(puuid).build());

        LocalDateTime now = LocalDateTime.now();

        switch (type) {
            case SUMMONER:
                update.setLastUpdatedSummoner(now);
                break;
            case RANKS:
                update.setLastUpdatedRanks(now);
                break;
            case MATCHES:
                update.setLastUpdatedMatches(now);
                break;
        }

        summonerUpdateRepository.save(update);
    }
}
