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

            if (!rootNode.isArray()) {
                return List.of();
            }

            return StreamSupport.stream(rootNode.spliterator(), false)
                    .map(LeagueMapper::toDto)
                    .filter(dto -> dto != null)
                    .distinct() // Add distinct to remove duplicates
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse ranked data response", e);
        }
    }

    private static LeagueDto toDto(JsonNode jsonNode) {
        try {
            JsonNode miniSeriesNode = jsonNode.get("miniSeries");
            LeagueDto.MiniSeriesDto miniSeries = null;

            if (miniSeriesNode != null && !miniSeriesNode.isNull()) {
                miniSeries = new LeagueDto.MiniSeriesDto(
                        getIntValue(miniSeriesNode, "wins"),
                        getValue(miniSeriesNode, "progress"),
                        getIntValue(miniSeriesNode, "target"),
                        getIntValue(miniSeriesNode, "losses"));
            } else {
                miniSeries = new LeagueDto.MiniSeriesDto(0, "", 0, 0);
            }

            return new LeagueDto(
                    getValue(jsonNode, "leagueId"),
                    getValue(jsonNode, "queueType"),
                    getValue(jsonNode, "tier"),
                    getValue(jsonNode, "rank"),
                    getValue(jsonNode, "summonerId"),
                    getValue(jsonNode, "puuid"),
                    getIntValue(jsonNode, "leaguePoints"),
                    getIntValue(jsonNode, "wins"),
                    getIntValue(jsonNode, "losses"),
                    getBoolValue(jsonNode, "veteran"),
                    getBoolValue(jsonNode, "inactive"),
                    getBoolValue(jsonNode, "freshBlood"),
                    getBoolValue(jsonNode, "hotStreak"),
                    miniSeries);
        } catch (Exception e) {
            return null;
        }
    }

    private static String getValue(JsonNode node, String fieldName) {
        JsonNode field = node.get(fieldName);
        return field != null && !field.isNull() ? field.asText() : null;
    }

    private static Integer getIntValue(JsonNode node, String fieldName) {
        JsonNode field = node.get(fieldName);
        return field != null && !field.isNull() ? field.asInt() : null;
    }

    private static Boolean getBoolValue(JsonNode node, String fieldName) {
        JsonNode field = node.get(fieldName);
        return field != null && !field.isNull() ? field.asBoolean() : null;
    }
}
