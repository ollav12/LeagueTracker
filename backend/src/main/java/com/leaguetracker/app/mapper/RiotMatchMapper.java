package com.leaguetracker.app.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leaguetracker.app.dto.MatchDto;
import com.leaguetracker.app.dto.MatchDto.BanDto;
import com.leaguetracker.app.dto.MatchDto.InfoDto;
import com.leaguetracker.app.dto.MatchDto.MetadataDto;
import com.leaguetracker.app.dto.MatchDto.ObjectivesDto;
import com.leaguetracker.app.dto.MatchDto.ParticipantDto;
import com.leaguetracker.app.dto.MatchDto.PerkStyleDto;
import com.leaguetracker.app.dto.MatchDto.PerkStyleSelectionDto;
import com.leaguetracker.app.dto.MatchDto.PerksDto;
import com.leaguetracker.app.dto.MatchDto.TeamDto;
import com.leaguetracker.app.dto.response.RiotMatchListResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

@Mapper
public interface RiotMatchMapper {

    RiotMatchMapper INSTANCE = Mappers.getMapper(RiotMatchMapper.class);
    ObjectMapper objectMapper = new ObjectMapper();

    // Map JsonNode to RiotMatchIdEntry
    String toRiotMatchIdEntry(JsonNode jsonNode);

    // Map JSON string to RiotMatchListResponse
    default RiotMatchListResponse toRiotMatchListResponse(String response) {
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            if (!rootNode.isArray()) {
                return new RiotMatchListResponse(List.of());
            }
            List<String> matches = StreamSupport.stream(rootNode.spliterator(), false)
                    .map(this::toRiotMatchIdEntry)
                    .filter(dto -> dto != null)
                    .distinct()
                    .toList();
            return new RiotMatchListResponse(matches);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse match list response", e);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    /// Match ///
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Mapping(target = "metadata", expression = "java(parseMetadata(jsonNode.path(\"metadata\")))")
    @Mapping(target = "info", expression = "java(parseInfo(jsonNode.path(\"info\")))")
    MatchDto toMatchDto(JsonNode jsonNode);

    default MatchDto apply(String response) {
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            return toMatchDto(jsonNode);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse response", e);
        }
    }

    default MetadataDto parseMetadata(JsonNode metadataNode) {
        return new MetadataDto(
                metadataNode.path("dataVersion").asText(),
                metadataNode.path("matchId").asText(),
                parseParticipantsString(metadataNode.path("participants")).toArray(new String[0]));
    }

    default List<String> parseParticipantsString(JsonNode participantsNode) {
        List<String> puuids = new ArrayList<>();
        if (participantsNode != null && participantsNode.isArray()) {
            for (JsonNode node : participantsNode) {
                String puuid = node.asText("");
                if (!puuid.isEmpty()) {
                    puuids.add(puuid);
                }
            }
        }
        return puuids;
    }

    default InfoDto parseInfo(JsonNode infoNode) {
        return new InfoDto(
                infoNode.path("endOfGameResult").asText(),
                infoNode.path("gameCreation").asLong(),
                infoNode.path("gameDuration").asLong(),
                infoNode.path("gameEndTimestamp").asLong(),
                infoNode.path("gameId").asLong(),
                infoNode.path("gameMode").asText(),
                infoNode.path("gameName").asText(),
                infoNode.path("gameStartTimestamp").asLong(),
                infoNode.path("gameType").asText(),
                infoNode.path("gameVersion").asText(),
                infoNode.path("mapId").asInt(),
                parseParticipants(infoNode.path("participants")),
                infoNode.path("platformId").asText(),
                infoNode.path("queueId").asInt(),
                parseTeams(infoNode.path("teams")),
                java.util.Optional.ofNullable(infoNode.path("tournamentCode").asText(null)));
    }

    @Mapping(target = "assists", expression = "java(participantNode.path(\"assists\").asInt())")
    @Mapping(target = "baronKills", expression = "java(participantNode.path(\"baronKills\").asInt())")
    @Mapping(target = "bountyLevel", expression = "java(participantNode.path(\"bountyLevel\").asInt())")
    @Mapping(target = "champExperience", expression = "java(participantNode.path(\"champExperience\").asInt())")
    @Mapping(target = "champLevel", expression = "java(participantNode.path(\"champLevel\").asInt())")
    @Mapping(target = "championId", expression = "java(participantNode.path(\"championId\").asInt())")
    @Mapping(target = "championName", expression = "java(participantNode.path(\"championName\").asText())")
    @Mapping(target = "championTransform", expression = "java(MatchDto.ChampionTransformDto.values()[participantNode.path(\"championTransform\").asInt()])")
    @Mapping(target = "consumablesPurchased", expression = "java(participantNode.path(\"consumablesPurchased\").asInt())")
    @Mapping(target = "damageDealtToObjectives", expression = "java(participantNode.path(\"damageDealtToObjectives\").asInt())")
    @Mapping(target = "damageDealtToTurrets", expression = "java(participantNode.path(\"damageDealtToTurrets\").asInt())")
    @Mapping(target = "damageSelfMitigated", expression = "java(participantNode.path(\"damageSelfMitigated\").asInt())")
    @Mapping(target = "deaths", expression = "java(participantNode.path(\"deaths\").asInt())")
    @Mapping(target = "detectorWardsPlaced", expression = "java(participantNode.path(\"detectorWardsPlaced\").asInt())")
    @Mapping(target = "doubleKills", expression = "java(participantNode.path(\"doubleKills\").asInt())")
    @Mapping(target = "dragonKills", expression = "java(participantNode.path(\"dragonKills\").asInt())")
    @Mapping(target = "firstBloodAssist", expression = "java(participantNode.path(\"firstBloodAssist\").asBoolean())")
    @Mapping(target = "firstBloodKill", expression = "java(participantNode.path(\"firstBloodKill\").asBoolean())")
    @Mapping(target = "firstTowerAssist", expression = "java(participantNode.path(\"firstTowerAssist\").asBoolean())")
    @Mapping(target = "firstTowerKill", expression = "java(participantNode.path(\"firstTowerKill\").asBoolean())")
    @Mapping(target = "gameEndedInEarlySurrender", expression = "java(participantNode.path(\"gameEndedInEarlySurrender\").asBoolean())")
    @Mapping(target = "gameEndedInSurrender", expression = "java(participantNode.path(\"gameEndedInSurrender\").asBoolean())")
    @Mapping(target = "goldEarned", expression = "java(participantNode.path(\"goldEarned\").asInt())")
    @Mapping(target = "goldSpent", expression = "java(participantNode.path(\"goldSpent\").asInt())")
    @Mapping(target = "individualPosition", expression = "java(participantNode.path(\"individualPosition\").asText())")
    @Mapping(target = "inhibitorKills", expression = "java(participantNode.path(\"inhibitorKills\").asInt())")
    @Mapping(target = "item0", expression = "java(participantNode.path(\"item0\").asInt())")
    @Mapping(target = "item1", expression = "java(participantNode.path(\"item1\").asInt())")
    @Mapping(target = "item2", expression = "java(participantNode.path(\"item2\").asInt())")
    @Mapping(target = "item3", expression = "java(participantNode.path(\"item3\").asInt())")
    @Mapping(target = "item4", expression = "java(participantNode.path(\"item4\").asInt())")
    @Mapping(target = "item5", expression = "java(participantNode.path(\"item5\").asInt())")
    @Mapping(target = "item6", expression = "java(participantNode.path(\"item6\").asInt())")
    @Mapping(target = "itemsPurchased", expression = "java(participantNode.path(\"itemsPurchased\").asInt())")
    @Mapping(target = "killingSprees", expression = "java(participantNode.path(\"killingSprees\").asInt())")
    @Mapping(target = "kills", expression = "java(participantNode.path(\"kills\").asInt())")
    @Mapping(target = "lane", expression = "java(participantNode.path(\"lane\").asText())")
    @Mapping(target = "largestCriticalStrike", expression = "java(participantNode.path(\"largestCriticalStrike\").asInt())")
    @Mapping(target = "largestKillingSpree", expression = "java(participantNode.path(\"largestKillingSpree\").asInt())")
    @Mapping(target = "largestMultiKill", expression = "java(participantNode.path(\"largestMultiKill\").asInt())")
    @Mapping(target = "longestTimeSpentLiving", expression = "java(participantNode.path(\"longestTimeSpentLiving\").asInt())")
    @Mapping(target = "magicDamageDealt", expression = "java(participantNode.path(\"magicDamageDealt\").asInt())")
    @Mapping(target = "magicDamageDealtToChampions", expression = "java(participantNode.path(\"magicDamageDealtToChampions\").asInt())")
    @Mapping(target = "magicDamageTaken", expression = "java(participantNode.path(\"magicDamageTaken\").asInt())")
    @Mapping(target = "neutralMinionsKilled", expression = "java(participantNode.path(\"neutralMinionsKilled\").asInt())")
    @Mapping(target = "nexusKills", expression = "java(participantNode.path(\"nexusKills\").asInt())")
    @Mapping(target = "objectivesStolen", expression = "java(participantNode.path(\"objectivesStolen\").asInt())")
    @Mapping(target = "objectivesStolenAssists", expression = "java(participantNode.path(\"objectivesStolenAssists\").asInt())")
    @Mapping(target = "participantId", expression = "java(participantNode.path(\"participantId\").asInt())")
    @Mapping(target = "pentaKills", expression = "java(participantNode.path(\"pentaKills\").asInt())")
    @Mapping(target = "perks", expression = "java(toPerksDto(participantNode.path(\"perks\")))")
    @Mapping(target = "physicalDamageDealt", expression = "java(participantNode.path(\"physicalDamageDealt\").asInt())")
    @Mapping(target = "physicalDamageDealtToChampions", expression = "java(participantNode.path(\"physicalDamageDealtToChampions\").asInt())")
    @Mapping(target = "physicalDamageTaken", expression = "java(participantNode.path(\"physicalDamageTaken\").asInt())")
    @Mapping(target = "profileIcon", expression = "java(participantNode.path(\"profileIcon\").asInt())")
    @Mapping(target = "puuid", expression = "java(participantNode.path(\"puuid\").asText(\"\"))")
    @Mapping(target = "quadraKills", expression = "java(participantNode.path(\"quadraKills\").asInt())")
    @Mapping(target = "riotIdGameName", expression = "java(participantNode.path(\"riotIdGameName\").asText())")
    @Mapping(target = "riotIdTagline", expression = "java(participantNode.path(\"riotIdTagline\").asText())")
    @Mapping(target = "role", expression = "java(MatchDto.RoleDto.valueOf(participantNode.path(\"role\").asText(\"NONE\").toUpperCase()))")
    @Mapping(target = "sightWardsBoughtInGame", expression = "java(participantNode.path(\"sightWardsBoughtInGame\").asInt())")
    @Mapping(target = "spell1Casts", expression = "java(participantNode.path(\"spell1Casts\").asInt())")
    @Mapping(target = "spell2Casts", expression = "java(participantNode.path(\"spell2Casts\").asInt())")
    @Mapping(target = "spell3Casts", expression = "java(participantNode.path(\"spell3Casts\").asInt())")
    @Mapping(target = "spell4Casts", expression = "java(participantNode.path(\"spell4Casts\").asInt())")
    @Mapping(target = "summoner1Casts", expression = "java(participantNode.path(\"summoner1Casts\").asInt())")
    @Mapping(target = "summoner1Id", expression = "java(participantNode.path(\"summoner1Id\").asInt())")
    @Mapping(target = "summoner2Casts", expression = "java(participantNode.path(\"summoner2Casts\").asInt())")
    @Mapping(target = "summoner2Id", expression = "java(participantNode.path(\"summoner2Id\").asInt())")
    @Mapping(target = "summonerId", expression = "java(participantNode.path(\"summonerId\").asText())")
    @Mapping(target = "summonerLevel", expression = "java(participantNode.path(\"summonerLevel\").asInt())")
    @Mapping(target = "summonerName", expression = "java(participantNode.path(\"summonerName\").asText())")
    @Mapping(target = "teamEarlySurrendered", expression = "java(participantNode.path(\"teamEarlySurrendered\").asBoolean())")
    @Mapping(target = "teamId", expression = "java(participantNode.path(\"teamId\").asInt())")
    @Mapping(target = "teamPosition", expression = "java(participantNode.path(\"teamPosition\").asText())")
    @Mapping(target = "timeCCingOthers", expression = "java(participantNode.path(\"timeCCingOthers\").asInt())")
    @Mapping(target = "timePlayed", expression = "java(participantNode.path(\"timePlayed\").asInt())")
    @Mapping(target = "totalDamageDealt", expression = "java(participantNode.path(\"totalDamageDealt\").asInt())")
    @Mapping(target = "totalDamageDealtToChampions", expression = "java(participantNode.path(\"totalDamageDealtToChampions\").asInt())")
    @Mapping(target = "totalDamageShieldedOnTeammates", expression = "java(participantNode.path(\"totalDamageShieldedOnTeammates\").asInt())")
    @Mapping(target = "totalDamageTaken", expression = "java(participantNode.path(\"totalDamageTaken\").asInt())")
    @Mapping(target = "totalHeal", expression = "java(participantNode.path(\"totalHeal\").asInt())")
    @Mapping(target = "totalHealsOnTeammates", expression = "java(participantNode.path(\"totalHealsOnTeammates\").asInt())")
    @Mapping(target = "totalMinionsKilled", expression = "java(participantNode.path(\"totalMinionsKilled\").asInt())")
    @Mapping(target = "totalTimeCCDealt", expression = "java(participantNode.path(\"totalTimeCCDealt\").asInt())")
    @Mapping(target = "totalTimeSpentDead", expression = "java(participantNode.path(\"totalTimeSpentDead\").asInt())")
    @Mapping(target = "totalUnitsHealed", expression = "java(participantNode.path(\"totalUnitsHealed\").asInt())")
    @Mapping(target = "tripleKills", expression = "java(participantNode.path(\"tripleKills\").asInt())")
    @Mapping(target = "trueDamageDealt", expression = "java(participantNode.path(\"trueDamageDealt\").asInt())")
    @Mapping(target = "trueDamageDealtToChampions", expression = "java(participantNode.path(\"trueDamageDealtToChampions\").asInt())")
    @Mapping(target = "trueDamageTaken", expression = "java(participantNode.path(\"trueDamageTaken\").asInt())")
    @Mapping(target = "turretKills", expression = "java(participantNode.path(\"turretKills\").asInt())")
    @Mapping(target = "unrealKills", expression = "java(participantNode.path(\"unrealKills\").asInt())")
    @Mapping(target = "visionScore", expression = "java(participantNode.path(\"visionScore\").asInt())")
    @Mapping(target = "visionWardsBoughtInGame", expression = "java(participantNode.path(\"visionWardsBoughtInGame\").asInt())")
    @Mapping(target = "wardsKilled", expression = "java(participantNode.path(\"wardsKilled\").asInt())")
    @Mapping(target = "wardsPlaced", expression = "java(participantNode.path(\"wardsPlaced\").asInt())")
    @Mapping(target = "win", expression = "java(participantNode.path(\"win\").asBoolean())")
    ParticipantDto toParticipantDto(JsonNode participantNode);

    default List<ParticipantDto> parseParticipants(JsonNode participantsNode) {
        List<ParticipantDto> participants = new ArrayList<>();
        for (JsonNode participantNode : participantsNode) {
            participants.add(toParticipantDto(participantNode));
        }
        return participants;
    }

    @Mapping(target = "statPerks", expression = "java(new MatchDto.PerkStatsDto(perksNode.path(\"statPerks\").path(\"defense\").asInt(), perksNode.path(\"statPerks\").path(\"flex\").asInt(), perksNode.path(\"statPerks\").path(\"offense\").asInt()))")
    @Mapping(target = "styles", expression = "java(parsePerkStyles(perksNode.path(\"styles\")))")
    PerksDto toPerksDto(JsonNode perksNode);

    default PerkStyleDto[] parsePerkStyles(JsonNode stylesNode) {
        List<PerkStyleDto> styles = new ArrayList<>();
        for (JsonNode styleNode : stylesNode) {
            styles.add(new PerkStyleDto(
                    styleNode.path("description").asText().toUpperCase(),
                    parsePerkStyleSelections(styleNode.path("selections")),
                    styleNode.path("style").asInt()));
        }
        return styles.toArray(new PerkStyleDto[0]);
    }

    default List<PerkStyleSelectionDto> parsePerkStyleSelections(JsonNode selectionsNode) {
        List<PerkStyleSelectionDto> selections = new ArrayList<>();
        for (JsonNode selectionNode : selectionsNode) {
            selections.add(new PerkStyleSelectionDto(
                    selectionNode.path("perk").asInt(),
                    selectionNode.path("var1").asInt(),
                    selectionNode.path("var2").asInt(),
                    selectionNode.path("var3").asInt()));
        }
        return selections;
    }

    @Mapping(target = "bans", expression = "java(parseBans(teamNode.path(\"bans\")))")
    @Mapping(target = "objectives", expression = "java(toObjectivesDto(teamNode.path(\"objectives\")))")
    @Mapping(target = "teamId", expression = "java(teamNode.path(\"teamId\").asInt())")
    @Mapping(target = "win", expression = "java(teamNode.path(\"win\").asBoolean())")
    TeamDto toTeamDto(JsonNode teamNode);

    default List<TeamDto> parseTeams(JsonNode teamsNode) {
        List<TeamDto> teams = new ArrayList<>();
        for (JsonNode teamNode : teamsNode) {
            teams.add(toTeamDto(teamNode));
        }
        return teams;
    }

    default BanDto[] parseBans(JsonNode bansNode) {
        List<BanDto> bans = new ArrayList<>();
        for (JsonNode banNode : bansNode) {
            bans.add(new BanDto(
                    banNode.path("championId").asInt(),
                    banNode.path("pickTurn").asInt()));
        }
        return bans.toArray(new BanDto[0]);
    }

    @Mapping(target = "baron", expression = "java(new MatchDto.ObjectiveDto(objectivesNode.path(\"baron\").path(\"first\").asBoolean(), objectivesNode.path(\"baron\").path(\"kills\").asInt()))")
    @Mapping(target = "champion", expression = "java(new MatchDto.ObjectiveDto(objectivesNode.path(\"champion\").path(\"first\").asBoolean(), objectivesNode.path(\"champion\").path(\"kills\").asInt()))")
    @Mapping(target = "dragon", expression = "java(new MatchDto.ObjectiveDto(objectivesNode.path(\"dragon\").path(\"first\").asBoolean(), objectivesNode.path(\"dragon\").path(\"kills\").asInt()))")
    @Mapping(target = "inhibitor", expression = "java(new MatchDto.ObjectiveDto(objectivesNode.path(\"inhibitor\").path(\"first\").asBoolean(), objectivesNode.path(\"inhibitor\").path(\"kills\").asInt()))")
    @Mapping(target = "riftHerald", expression = "java(new MatchDto.ObjectiveDto(objectivesNode.path(\"riftHerald\").path(\"first\").asBoolean(), objectivesNode.path(\"riftHerald\").path(\"kills\").asInt()))")
    @Mapping(target = "tower", expression = "java(new MatchDto.ObjectiveDto(objectivesNode.path(\"tower\").path(\"first\").asBoolean(), objectivesNode.path(\"tower\").path(\"kills\").asInt()))")
    ObjectivesDto toObjectivesDto(JsonNode objectivesNode);
}