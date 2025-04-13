package com.leaguetracker.app.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leaguetracker.app.dto.response.RiotAccountResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RiotAccountMapper {

    RiotAccountMapper INSTANCE = Mappers.getMapper(RiotAccountMapper.class);

    @Mapping(target = "puuid", expression = "java(jsonNode.get(\"puuid\") != null ? jsonNode.get(\"puuid\").asText() : null)")
    @Mapping(target = "gameName", expression = "java(jsonNode.get(\"gameName\") != null ? jsonNode.get(\"gameName\").asText() : null)")
    @Mapping(target = "tag", expression = "java(jsonNode.get(\"tagLine\") != null ? jsonNode.get(\"tagLine\").asText() : null)")
    RiotAccountResponse toRiotAccountResponse(JsonNode jsonNode);

    default RiotAccountResponse toRiotAccountResponse(String response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);
            return toRiotAccountResponse(jsonNode);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse account response", e);
        }
    }
}