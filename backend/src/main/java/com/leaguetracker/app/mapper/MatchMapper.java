package com.leaguetracker.app.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leaguetracker.app.dto.*;
import com.leaguetracker.app.dto.MatchDto.BanDto;
import com.leaguetracker.app.dto.MatchDto.ChampionTransformDto;
import com.leaguetracker.app.dto.MatchDto.InfoDto;
import com.leaguetracker.app.dto.MatchDto.LaneDto;
import com.leaguetracker.app.dto.MatchDto.ParticipantDto;
import com.leaguetracker.app.dto.MatchDto.PerkStatsDto;
import com.leaguetracker.app.dto.MatchDto.PerkStyleDto;
import com.leaguetracker.app.dto.MatchDto.PerkStyleSelectionDto;
import com.leaguetracker.app.dto.MatchDto.PerksDto;
import com.leaguetracker.app.dto.MatchDto.RoleDto;
import com.leaguetracker.app.dto.MatchDto.TeamDto;
import com.leaguetracker.app.dto.MatchDto.TeamPositionDto;
import com.leaguetracker.app.dto.MatchDto.MetadataDto;
import com.leaguetracker.app.dto.MatchDto.ObjectiveDto;
import com.leaguetracker.app.dto.MatchDto.ObjectivesDto;

public class MatchMapper implements Function<String, MatchDto> {

    private final ObjectMapper objectMapper;

    public MatchMapper() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public MatchDto apply(String response) {
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            // Parse Metadata
            JsonNode metadataNode = jsonNode.path("metadata");
            List<String> participants = parseParticipantsString(metadataNode.path("participants"));
            MetadataDto metadataDto = new MetadataDto(
                    metadataNode.path("dataVersion").asText(),
                    metadataNode.path("matchId").asText(),
                    participants.toArray(new String[0]));

            // Parse Info
            JsonNode infoNode = jsonNode.path("info");
            InfoDto infoDto = new InfoDto(
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
                    Optional.ofNullable(infoNode.path("tournamentCode").asText(null)));

            return new MatchDto(metadataDto, infoDto);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse response", e);
        }
    }

    private List<String> parseParticipantsString(JsonNode participantsNode) {
        return participantsNode.findValuesAsText("puuid"); // Assuming 'puuid' is the field we need
    }

    // Helper function to parse participants
    private List<ParticipantDto> parseParticipants(JsonNode participantsNode) {
        List<ParticipantDto> participants = new ArrayList<>();
        for (JsonNode participantNode : participantsNode) {
            participants.add(new ParticipantDto(
                    participantNode.path("assists").asInt(),
                    participantNode.path("baronKills").asInt(),
                    participantNode.path("bountyLevel").asInt(),
                    participantNode.path("champExperience").asInt(),
                    participantNode.path("champLevel").asInt(),
                    participantNode.path("championId").asInt(),
                    participantNode.path("championName").asText(),
                    ChampionTransformDto.values()[participantNode.path("championTransform").asInt()],
                    participantNode.path("consumablesPurchased").asInt(),
                    participantNode.path("damageDealtToObjectives").asInt(),
                    participantNode.path("damageDealtToTurrets").asInt(),
                    participantNode.path("damageSelfMitigated").asInt(),
                    participantNode.path("deaths").asInt(),
                    participantNode.path("detectorWardsPlaced").asInt(),
                    participantNode.path("doubleKills").asInt(),
                    participantNode.path("dragonKills").asInt(),
                    participantNode.path("firstBloodAssist").asBoolean(),
                    participantNode.path("firstBloodKill").asBoolean(),
                    participantNode.path("firstTowerAssist").asBoolean(),
                    participantNode.path("firstTowerKill").asBoolean(),
                    participantNode.path("gameEndedInEarlySurrender").asBoolean(),
                    participantNode.path("gameEndedInSurrender").asBoolean(),
                    participantNode.path("goldEarned").asInt(),
                    participantNode.path("goldSpent").asInt(),
                    participantNode.path("individualPosition").asText(),
                    participantNode.path("inhibitorKills").asInt(),
                    participantNode.path("item0").asInt(),
                    participantNode.path("item1").asInt(),
                    participantNode.path("item2").asInt(),
                    participantNode.path("item3").asInt(),
                    participantNode.path("item4").asInt(),
                    participantNode.path("item5").asInt(),
                    participantNode.path("item6").asInt(),
                    participantNode.path("itemsPurchased").asInt(),
                    participantNode.path("killingSprees").asInt(),
                    participantNode.path("kills").asInt(),
                    participantNode.path("lane").asText(),
                    participantNode.path("largestCriticalStrike").asInt(),
                    participantNode.path("largestKillingSpree").asInt(),
                    participantNode.path("largestMultiKill").asInt(),
                    participantNode.path("longestTimeSpentLiving").asInt(),
                    participantNode.path("magicDamageDealt").asInt(),
                    participantNode.path("magicDamageDealtToChampions").asInt(),
                    participantNode.path("magicDamageTaken").asInt(),
                    participantNode.path("neutralMinionsKilled").asInt(),
                    participantNode.path("nexusKills").asInt(),
                    participantNode.path("objectivesStolen").asInt(),
                    participantNode.path("objectivesStolenAssists").asInt(),
                    participantNode.path("participantId").asInt(),
                    participantNode.path("pentaKills").asInt(),
                    parsePerks(participantNode.path("perks")),
                    participantNode.path("physicalDamageDealt").asInt(),
                    participantNode.path("physicalDamageDealtToChampions").asInt(),
                    participantNode.path("physicalDamageTaken").asInt(),
                    participantNode.path("profileIcon").asInt(),
                    participantNode.path("puuid").asText(),
                    participantNode.path("quadraKills").asInt(),
                    participantNode.path("riotIdGameName").asText(),
                    participantNode.path("riotIdTagline").asText(),
                    RoleDto.valueOf(participantNode.path("role").asText("NONE").toUpperCase()),
                    participantNode.path("sightWardsBoughtInGame").asInt(),
                    participantNode.path("spell1Casts").asInt(),
                    participantNode.path("spell2Casts").asInt(),
                    participantNode.path("spell3Casts").asInt(),
                    participantNode.path("spell4Casts").asInt(),
                    participantNode.path("summoner1Casts").asInt(),
                    participantNode.path("summoner1Id").asInt(),
                    participantNode.path("summoner2Casts").asInt(),
                    participantNode.path("summoner2Id").asInt(),
                    participantNode.path("summonerId").asText(),
                    participantNode.path("summonerLevel").asInt(),
                    participantNode.path("summonerName").asText(),
                    participantNode.path("teamEarlySurrendered").asBoolean(),
                    participantNode.path("teamId").asInt(),
                    participantNode.path("teamPosition").asText(),
                    participantNode.path("timeCCingOthers").asInt(),
                    participantNode.path("timePlayed").asInt(),
                    participantNode.path("totalDamageDealt").asInt(),
                    participantNode.path("totalDamageDealtToChampions").asInt(),
                    participantNode.path("totalDamageShieldedOnTeammates").asInt(),
                    participantNode.path("totalDamageTaken").asInt(),
                    participantNode.path("totalHeal").asInt(),
                    participantNode.path("totalHealsOnTeammates").asInt(),
                    participantNode.path("totalMinionsKilled").asInt(),
                    participantNode.path("totalTimeCCDealt").asInt(),
                    participantNode.path("totalTimeSpentDead").asInt(),
                    participantNode.path("totalUnitsHealed").asInt(),
                    participantNode.path("tripleKills").asInt(),
                    participantNode.path("trueDamageDealt").asInt(),
                    participantNode.path("trueDamageDealtToChampions").asInt(),
                    participantNode.path("trueDamageTaken").asInt(),
                    participantNode.path("turretKills").asInt(),
                    participantNode.path("unrealKills").asInt(),
                    participantNode.path("visionScore").asInt(),
                    participantNode.path("visionWardsBoughtInGame").asInt(),
                    participantNode.path("wardsKilled").asInt(),
                    participantNode.path("wardsPlaced").asInt(),
                    participantNode.path("win").asBoolean()));
        }
        return participants;
    }

    // Helper function to parse perks
    private PerksDto parsePerks(JsonNode perksNode) {
        // Parse PerkStats
        PerkStatsDto statPerks = new PerkStatsDto(
                perksNode.path("statPerks").path("defense").asInt(),
                perksNode.path("statPerks").path("flex").asInt(),
                perksNode.path("statPerks").path("offense").asInt());

        // Parse PerkStyleDto[]
        List<PerkStyleDto> styles = new ArrayList<>();
        for (JsonNode styleNode : perksNode.path("styles")) {
            List<PerkStyleSelectionDto> selections = new ArrayList<>();
            for (JsonNode selectionNode : styleNode.path("selections")) {
                selections.add(new PerkStyleSelectionDto(
                        selectionNode.path("perk").asInt(),
                        selectionNode.path("var1").asInt(),
                        selectionNode.path("var2").asInt(),
                        selectionNode.path("var3").asInt()));
            }
            styles.add(new PerkStyleDto(
                    (styleNode.path("description").asText().toUpperCase()), selections,
                    styleNode.path("style").asInt()));
        }

        return new PerksDto(statPerks, styles.toArray(new PerkStyleDto[0]));
    }

    // Helper function to parse teams
    private List<TeamDto> parseTeams(JsonNode teamsNode) {
        List<TeamDto> teams = new ArrayList<>();
        for (JsonNode teamNode : teamsNode) {
            teams.add(new TeamDto(
                    parseBans(teamNode.path("bans")),
                    parseObjectives(teamNode.path("objectives")),
                    teamNode.path("teamId").asInt(),
                    teamNode.path("win").asBoolean()));
        }
        return teams;
    }

    // Helper function to parse bans
    private BanDto[] parseBans(JsonNode bansNode) {
        List<BanDto> bans = new ArrayList<>();
        for (JsonNode banNode : bansNode) {
            bans.add(new BanDto(
                    banNode.path("championId").asInt(),
                    banNode.path("pickTurn").asInt()));
        }
        return bans.toArray(new BanDto[0]);
    }

    // Helper function to parse objectives
    private ObjectivesDto parseObjectives(JsonNode objectivesNode) {
        return new ObjectivesDto(
                new ObjectiveDto(objectivesNode.path("baron").path("first").asBoolean(),
                        objectivesNode.path("baron").path("kills").asInt()),
                new ObjectiveDto(objectivesNode.path("champion").path("first").asBoolean(),
                        objectivesNode.path("champion").path("kills").asInt()),
                new ObjectiveDto(objectivesNode.path("dragon").path("first").asBoolean(),
                        objectivesNode.path("dragon").path("kills").asInt()),
                new ObjectiveDto(objectivesNode.path("inhibitor").path("first").asBoolean(),
                        objectivesNode.path("inhibitor").path("kills").asInt()),
                new ObjectiveDto(objectivesNode.path("riftHerald").path("first").asBoolean(),
                        objectivesNode.path("riftHerald").path("kills").asInt()),
                new ObjectiveDto(objectivesNode.path("tower").path("first").asBoolean(),
                        objectivesNode.path("tower").path("kills").asInt()));
    }
}
