package com.leaguetracker.app.mapper;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leaguetracker.app.dto.LeagueDto;

public class LeagueMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<LeagueDto> toDtoList(String response) {
        try {
            JsonNode rootNode = objectMapper.readTree(response);

            // Riot API returns an array, so we map each item to LeagueDto
            if (rootNode.isArray()) {
                return StreamSupport.stream(rootNode.spliterator(), false)
                        .map(LeagueMapper::toDto)
                        .collect(Collectors.toList());
            }
            return List.of();

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse ranked data response", e);
        }
    }

    private static LeagueDto toDto(JsonNode jsonNode) {
        return new LeagueDto(
                jsonNode.get("leagueId").asText(),
                jsonNode.get("queueType").asText(),
                jsonNode.get("tier").asText(),
                jsonNode.get("rank").asText(),
                jsonNode.get("summonerId").asText(),
                jsonNode.get("puuid").asText(),
                jsonNode.get("leaguePoints").asText(),
                jsonNode.get("wins").asInt(),
                jsonNode.get("losses").asInt(),
                jsonNode.get("veteran").asBoolean(),
                jsonNode.get("inactive").asBoolean(),
                jsonNode.get("freshBlood").asBoolean(),
                jsonNode.get("hotStreak").asBoolean());

    }
}
