package com.leaguetracker.app.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leaguetracker.app.dto.response.RiotLeagueResponse;
import com.leaguetracker.app.dto.response.RiotSummonerResponse;
import com.leaguetracker.app.dto.response.SummonerLookupResponse;
import com.leaguetracker.app.model.Summoner;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RiotSummonerMapper {

    RiotSummonerMapper INSTANCE = Mappers.getMapper(RiotSummonerMapper.class);

    @Mapping(target = "id", expression = "java(jsonNode.get(\"id\") != null ? jsonNode.get(\"id\").asText() : null)")
    @Mapping(target = "accountId", expression = "java(jsonNode.get(\"accountId\") != null ? jsonNode.get(\"accountId\").asText() : null)")
    @Mapping(target = "puuid", expression = "java(jsonNode.get(\"puuid\") != null ? jsonNode.get(\"puuid\").asText() : null)")
    @Mapping(target = "profileIconId", expression = "java(jsonNode.get(\"profileIconId\") != null ? jsonNode.get(\"profileIconId\").asInt() : 0)")
    @Mapping(target = "revisionDate", expression = "java(jsonNode.get(\"revisionDate\") != null ? jsonNode.get(\"revisionDate\").asInt() : 0)")
    @Mapping(target = "summonerLevel", expression = "java(jsonNode.get(\"summonerLevel\") != null ? jsonNode.get(\"summonerLevel\").asInt() : 0)")
    RiotSummonerResponse jsonNodeToSummonerDto(JsonNode jsonNode);

    @Mapping(target = "summonerName", source = "name")
    @Mapping(target = "tagLine", source = "tagLine")
    @Mapping(target = "id", source = "response.id")
    @Mapping(target = "accountId", source = "response.accountId")
    @Mapping(target = "puuid", source = "response.puuid")
    @Mapping(target = "profileIconId", source = "response.profileIconId")
    @Mapping(target = "revisionDate", source = "response.revisionDate")
    @Mapping(target = "summonerLevel", source = "response.summonerLevel")
    @Mapping(target = "ranked", source = "leagues.leagues")
    SummonerLookupResponse toResponse(String name, String tagLine, RiotSummonerResponse response,
            RiotLeagueResponse leagues);

    @Mapping(target = "summonerName", source = "summoner.summonerName")
    @Mapping(target = "region", source = "region")
    @Mapping(target = "profileIconId", source = "summoner.profileIconId")
    @Mapping(target = "summonerLevel", source = "summoner.summonerLevel")
    @Mapping(target = "revisionDate", source = "summoner.revisionDate")
    @Mapping(target = "accountId", source = "summoner.accountId")
    @Mapping(target = "id", source = "summoner.id")
    @Mapping(target = "tagLine", source = "summoner.tagLine")
    @Mapping(target = "lastUpdated", expression = "java(new java.util.Date())")
    Summoner toEntity(String region, SummonerLookupResponse summoner);

    @Named("jsonNodeToString")
    default String jsonNodeToString(JsonNode node) {
        return node != null ? node.asText() : null;
    }

    @Named("jsonNodeToInt")
    default int jsonNodeToInt(JsonNode node) {
        return node != null ? node.asInt() : 0;
    }

    default RiotSummonerResponse toSummonerDto(String response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);
            return jsonNodeToSummonerDto(jsonNode);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse response", e);
        }
    }
}
