package com.leaguetracker.app.service;

import com.leaguetracker.app.repository.RankRepository;
import com.leaguetracker.app.service.riot.RiotService;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.leaguetracker.app.dto.RiotLeagueEntry;
import com.leaguetracker.app.dto.response.RiotLeagueResponse;
import com.leaguetracker.app.mapper.RiotLeagueMapper;
import com.leaguetracker.app.model.SummonerRank;
import com.leaguetracker.app.model.SummonerRank.MiniSeries;

@Service
@RequiredArgsConstructor
public class RankService {

    private final RankRepository rankRepository;
    private final RiotService riotService;

    public void saveLeagueDto(RiotLeagueResponse ranks) {
        for (RiotLeagueEntry rank : ranks.leagues()) {
            MiniSeries miniSeries;
            if (rank.miniSeries() == null) {
                miniSeries = new MiniSeries(0, "", 0, 0);
            } else {
                miniSeries = new MiniSeries(rank.miniSeries().wins(), rank.miniSeries().progress(),
                        rank.miniSeries().target(),
                        rank.miniSeries().losses());
            }
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
                    rank.hotStreak(),
                    miniSeries);
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
        return rankRepository.findAllByPuuid(puuid);
    }

    public RiotLeagueResponse getRanked(String puuid, String region) {
        List<SummonerRank> leaguesDb = rankRepository.findAllByPuuid(puuid);

        if (leaguesDb != null && !leaguesDb.isEmpty()) {
            System.out.println("Retrieved ranked data from database: " + leaguesDb.size() + " entries");
            return RiotLeagueMapper.INSTANCE.toResponse(leaguesDb);
        }

        RiotLeagueResponse leaguesFetched = riotService.League.findByPuuid(puuid, region);
        System.out.println("Fetched ranked data from Riot API entries");

        saveLeagueDto(leaguesFetched);
        return leaguesFetched;
    }

    public RiotLeagueResponse fetchSummonerLeague(String puuid, String region) {
        return riotService.League.findByPuuid(puuid, region);
    }
}
