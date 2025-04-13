package com.leaguetracker.app.mapper;

import com.leaguetracker.app.dto.response.RiotAccountResponse;
import com.leaguetracker.app.dto.response.RiotLeagueResponse;
import com.leaguetracker.app.dto.response.RiotSummonerResponse;
import com.leaguetracker.app.dto.response.SummonerLookupResponse;
import com.leaguetracker.app.model.Summoner;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RiotSummonerMapper {

    RiotSummonerMapper INSTANCE = Mappers.getMapper(RiotSummonerMapper.class);

    @Mapping(target = "puuid", source = "account.puuid")
    @Mapping(target = "summonerName", source = "account.gameName")
    @Mapping(target = "ranked", source = "leagues.leagues")
    SummonerLookupResponse toSummonerLookupResponse(
            RiotAccountResponse account,
            RiotSummonerResponse response,
            RiotLeagueResponse leagues);

    RiotSummonerResponse toRiotSummonerResponse(Summoner summoner);

    Summoner toSummoner(RiotSummonerResponse summoner);
}
