package com.leaguetracker.app.mapper;

import java.util.function.Function;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leaguetracker.app.dto.SummonerDto;
import com.leaguetracker.app.dto.response.SummonerResponse;

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

    public static SummonerResponse toResponse(String name, String tagLine, SummonerDto dto) {
        return new SummonerResponse(
                name, tagLine,
                dto.id(), dto.accountId(), dto.puuid(),
                dto.profieIconId(), dto.revisionDate(), dto.summonerLevel());
    }
}
