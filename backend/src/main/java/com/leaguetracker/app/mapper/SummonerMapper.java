package com.leaguetracker.app.mapper;

import java.util.List;
import java.util.function.Function;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leaguetracker.app.dto.LeagueDto;
import com.leaguetracker.app.dto.SummonerDto;
import com.leaguetracker.app.dto.response.SummonerResponse;
import com.leaguetracker.app.model.Summoner;

public class SummonerMapper implements Function<String, SummonerDto> {

    private final ObjectMapper objectMapper;

    public SummonerMapper() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public SummonerDto apply(String response) {
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            SummonerDto summonerDto = new SummonerDto(
                    jsonNode.get("id").asText(),
                    jsonNode.get("accountId").asText(),
                    jsonNode.get("puuid").asText(),
                    jsonNode.get("profileIconId").asInt(),
                    jsonNode.get("revisionDate").asInt(),
                    jsonNode.get("summonerLevel").asInt());
            return summonerDto;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse response", e);
        }
    }

    public static SummonerResponse toResponse(String name, String tagLine, SummonerDto dto, List<LeagueDto> ranked) {
        return new SummonerResponse(
                name,
                tagLine,
                dto.id(),
                dto.accountId(),
                dto.puuid(),
                dto.profieIconId(),
                dto.revisionDate(),
                dto.summonerLevel(),
                ranked);
    }

    public static Summoner toEntity(String region, SummonerResponse summoner) {
        return new Summoner(
                summoner.puuid(),
                summoner.summonerName(),
                region,
                summoner.profileIconId(),
                summoner.summonerLevel(),
                summoner.revisionDate(),
                summoner.accountId(),
                summoner.id(),
                summoner.tagLine());
    }
}
