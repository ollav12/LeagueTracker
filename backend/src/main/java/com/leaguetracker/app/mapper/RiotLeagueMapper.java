package com.leaguetracker.app.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leaguetracker.app.dto.RiotLeagueEntry;
import com.leaguetracker.app.dto.response.RiotLeagueResponse;
import com.leaguetracker.app.model.SummonerRank;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.StreamSupport;

@Mapper
public interface RiotLeagueMapper {

    RiotLeagueMapper INSTANCE = Mappers.getMapper(RiotLeagueMapper.class);

    @Mapping(target = "leagueId", expression = "java(jsonNode.get(\"leagueId\") != null ? jsonNode.get(\"leagueId\").asText() : null)")
    @Mapping(target = "queueType", expression = "java(jsonNode.get(\"queueType\") != null ? jsonNode.get(\"queueType\").asText() : null)")
    @Mapping(target = "tier", expression = "java(jsonNode.get(\"tier\") != null ? jsonNode.get(\"tier\").asText() : null)")
    @Mapping(target = "rank", expression = "java(jsonNode.get(\"rank\") != null ? jsonNode.get(\"rank\").asText() : null)")
    @Mapping(target = "summonerId", expression = "java(jsonNode.get(\"summonerId\") != null ? jsonNode.get(\"summonerId\").asText() : null)")
    @Mapping(target = "puuid", expression = "java(jsonNode.get(\"puuid\") != null ? jsonNode.get(\"puuid\").asText() : null)")
    @Mapping(target = "leaguePoints", expression = "java(jsonNode.get(\"leaguePoints\") != null ? jsonNode.get(\"leaguePoints\").asInt() : null)")
    @Mapping(target = "wins", expression = "java(jsonNode.get(\"wins\") != null ? jsonNode.get(\"wins\").asInt() : null)")
    @Mapping(target = "losses", expression = "java(jsonNode.get(\"losses\") != null ? jsonNode.get(\"losses\").asInt() : null)")
    @Mapping(target = "veteran", expression = "java(jsonNode.get(\"veteran\") != null ? jsonNode.get(\"veteran\").asBoolean() : null)")
    @Mapping(target = "inactive", expression = "java(jsonNode.get(\"inactive\") != null ? jsonNode.get(\"inactive\").asBoolean() : null)")
    @Mapping(target = "freshBlood", expression = "java(jsonNode.get(\"freshBlood\") != null ? jsonNode.get(\"freshBlood\").asBoolean() : null)")
    @Mapping(target = "hotStreak", expression = "java(jsonNode.get(\"hotStreak\") != null ? jsonNode.get(\"hotStreak\").asBoolean() : null)")
    @Mapping(target = "miniSeries", expression = "java(jsonNode.get(\"miniSeries\") != null ? INSTANCE.mapMiniSeries(jsonNode.get(\"miniSeries\")) : new RiotLeagueEntry.MiniSeriesDto(0, \"\", 0, 0))")
    RiotLeagueEntry toRiotLeagueEntry(JsonNode jsonNode);

    default RiotLeagueResponse toRiotLeagueResponse(String response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);
            if (!rootNode.isArray()) {
                return new RiotLeagueResponse(List.of());
            }
            List<RiotLeagueEntry> leagues = StreamSupport.stream(rootNode.spliterator(), false)
                    .map(this::toRiotLeagueEntry)
                    .filter(dto -> dto != null)
                    .distinct()
                    .toList();
            return new RiotLeagueResponse(leagues);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse ranked data response", e);
        }
    }

    @Named("mapMiniSeries")
    default RiotLeagueEntry.MiniSeriesDto mapMiniSeries(JsonNode miniSeriesNode) {
        if (miniSeriesNode == null || miniSeriesNode.isNull()) {
            return new RiotLeagueEntry.MiniSeriesDto(0, "", 0, 0);
        }
        return new RiotLeagueEntry.MiniSeriesDto(
                miniSeriesNode.get("wins") != null ? miniSeriesNode.get("wins").asInt() : 0,
                miniSeriesNode.get("progress") != null ? miniSeriesNode.get("progress").asText() : "",
                miniSeriesNode.get("target") != null ? miniSeriesNode.get("target").asInt() : 0,
                miniSeriesNode.get("losses") != null ? miniSeriesNode.get("losses").asInt() : 0);
    }

    @Mapping(target = "leagueId", source = "leagueId")
    @Mapping(target = "queueType", source = "queueType")
    @Mapping(target = "tier", source = "tier")
    @Mapping(target = "rank", source = "rank")
    @Mapping(target = "summonerId", source = "summonerId")
    @Mapping(target = "puuid", source = "puuid")
    @Mapping(target = "leaguePoints", source = "leaguePoints")
    @Mapping(target = "wins", source = "wins")
    @Mapping(target = "losses", source = "losses")
    @Mapping(target = "veteran", source = "veteran")
    @Mapping(target = "inactive", source = "inactive")
    @Mapping(target = "freshBlood", source = "freshBlood")
    @Mapping(target = "hotStreak", source = "hotStreak")
    @Mapping(target = "miniSeries", source = "miniSeries")
    RiotLeagueEntry toRiotLeagueEntry(SummonerRank summonerRank);

    RiotLeagueEntry.MiniSeriesDto toMiniSeriesDto(SummonerRank.MiniSeries miniSeries);

    default RiotLeagueResponse toResponse(List<SummonerRank> summonerRanks) {
        if (summonerRanks == null) {
            return new RiotLeagueResponse(List.of());
        }
        List<RiotLeagueEntry> leagues = summonerRanks.stream()
                .map(this::toRiotLeagueEntry)
                .filter(dto -> dto != null)
                .distinct()
                .toList();
        return new RiotLeagueResponse(leagues);
    }

    @Named("getStringValue")
    default String getStringValue(JsonNode node, String fieldName) {
        JsonNode field = node.get(fieldName);
        return field != null && !field.isNull() ? field.asText() : null;
    }

    @Named("getIntValue")
    default Integer getIntValue(JsonNode node, String fieldName) {
        JsonNode field = node.get(fieldName);
        return field != null && !field.isNull() ? field.asInt() : null;
    }

    @Named("getBoolValue")
    default Boolean getBoolValue(JsonNode node, String fieldName) {
        JsonNode field = node.get(fieldName);
        return field != null && !field.isNull() ? field.asBoolean() : null;
    }
}