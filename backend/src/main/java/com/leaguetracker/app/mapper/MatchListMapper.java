package com.leaguetracker.app.mapper;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leaguetracker.app.dto.MatchListDto;

public class MatchListMapper implements Function<String, List<MatchListDto>> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<MatchListDto> apply(String response) {
        try {
            JsonNode rootNode = objectMapper.readTree(response);

            if (rootNode.isArray()) {
                return StreamSupport.stream(rootNode.spliterator(), false)
                        .map(node -> new MatchListDto("", node.asText()))
                        .collect(Collectors.toList());
            }

            return List.of();
        } catch (Exception e) {
            throw new RuntimeException("Error mapping match list response", e);
        }
    }
}
