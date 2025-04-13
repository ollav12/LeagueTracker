package com.leaguetracker.app.mapper;

import com.leaguetracker.app.dto.response.RiotLeagueResponse.RiotLeagueEntry;
import com.leaguetracker.app.dto.response.RiotLeagueResponse.RiotLeagueEntry.MiniSeriesDto;
import com.leaguetracker.app.dto.response.RiotLeagueResponse;
import com.leaguetracker.app.model.SummonerRank;
import com.leaguetracker.app.model.SummonerRank.MiniSeries;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface RiotLeagueMapper {

    RiotLeagueMapper INSTANCE = Mappers.getMapper(RiotLeagueMapper.class);

    RiotLeagueEntry toRiotLeagueEntry(SummonerRank summonerRank);

    MiniSeriesDto toMiniSeriesDto(MiniSeries miniSeries);

    default RiotLeagueResponse toResponse(List<SummonerRank> summonerRanks) {
        return RiotLeagueResponse.builder()
                .leagues(summonerRanks.stream()
                        .map(this::toRiotLeagueEntry)
                        .filter(dto -> dto != null)
                        .distinct()
                        .toList())
                .build();
    }
}