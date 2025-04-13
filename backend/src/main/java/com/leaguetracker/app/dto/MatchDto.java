package com.leaguetracker.app.dto;

import java.util.List;
import java.util.Optional;

public record MatchDto(
                MatchDto.MetadataDto metadata,
                MatchDto.InfoDto info) {

        public static record MetadataDto(String dataVersion, String matchId, String[] participants) {
        }

        public static record InfoDto(
                        String endOfGameResult,
                        long gameCreation,
                        long gameDuration,
                        long gameEndTimestamp,
                        long gameId,
                        String gameMode,
                        String gameName,
                        long gameStartTimestamp,
                        String gameType,
                        String gameVersion,
                        int mapId,
                        List<ParticipantDto> participants,
                        String platformId,
                        int queueId,
                        List<TeamDto> teams,
                        Optional<String> tournamentCode) {
        }

        public static record ParticipantDto(
                        int assists,
                        int baronKills,
                        int bountyLevel,
                        int champExperience,
                        int champLevel,
                        int championId,
                        String championName,
                        ChampionTransformDto championTransform,
                        int consumablesPurchased,
                        int damageDealtToObjectives,
                        int damageDealtToTurrets,
                        int damageSelfMitigated,
                        int deaths,
                        int detectorWardsPlaced,
                        int doubleKills,
                        int dragonKills,
                        boolean firstBloodAssist,
                        boolean firstBloodKill,
                        boolean firstTowerAssist,
                        boolean firstTowerKill,
                        boolean gameEndedInEarlySurrender,
                        boolean gameEndedInSurrender,
                        int goldEarned,
                        int goldSpent,
                        String individualPosition, // Should be changed if TeamPositionDto is defined
                        int inhibitorKills,
                        int item0,
                        int item1,
                        int item2,
                        int item3,
                        int item4,
                        int item5,
                        int item6,
                        int itemsPurchased,
                        int killingSprees,
                        int kills,
                        String lane,
                        int largestCriticalStrike,
                        int largestKillingSpree,
                        int largestMultiKill,
                        int longestTimeSpentLiving,
                        int magicDamageDealt,
                        int magicDamageDealtToChampions,
                        int magicDamageTaken,
                        int neutralMinionsKilled,
                        int nexusKills,
                        int objectivesStolen,
                        int objectivesStolenAssists,
                        int participantId,
                        int pentaKills,
                        PerksDto perks,
                        int physicalDamageDealt,
                        int physicalDamageDealtToChampions,
                        int physicalDamageTaken,
                        int profileIcon,
                        String puuid,
                        int quadraKills,
                        String riotIdGameName,
                        String riotIdTagline,
                        RoleDto role,
                        int sightWardsBoughtInGame,
                        int spell1Casts,
                        int spell2Casts,
                        int spell3Casts,
                        int spell4Casts,
                        int summoner1Casts,
                        int summoner1Id,
                        int summoner2Casts,
                        int summoner2Id,
                        String summonerId,
                        int summonerLevel,
                        String summonerName,
                        boolean teamEarlySurrendered,
                        int teamId,
                        String teamPosition,
                        int timeCCingOthers,
                        int timePlayed,
                        int totalDamageDealt,
                        int totalDamageDealtToChampions,
                        int totalDamageShieldedOnTeammates,
                        int totalDamageTaken,
                        int totalHeal,
                        int totalHealsOnTeammates,
                        int totalMinionsKilled,
                        int totalTimeCCDealt,
                        int totalTimeSpentDead,
                        int totalUnitsHealed,
                        int tripleKills,
                        int trueDamageDealt,
                        int trueDamageDealtToChampions,
                        int trueDamageTaken,
                        int turretKills,
                        int unrealKills,
                        int visionScore,
                        int visionWardsBoughtInGame,
                        int wardsKilled,
                        int wardsPlaced,
                        boolean win) {

        }

        public static record TeamDto(
                        BanDto[] bans,
                        ObjectivesDto objectives,
                        int teamId,
                        boolean win) {
        }

        public static record ObjectivesDto(
                        ObjectiveDto baron,
                        ObjectiveDto champion,
                        ObjectiveDto dragon,
                        ObjectiveDto inhibitor,
                        ObjectiveDto riftHerald,
                        ObjectiveDto tower) {
        }

        public static record ObjectiveDto(
                        boolean first,
                        int kills) {
        }

        public static enum ChampionTransformDto {
                None,
                Slayer,
                Asassin
        }

        public static enum TeamPositionDto {
                TOP,
                JUNGLE,
                MIDDLE,
                BOTTOM,
                UTILITY
        }

        public static record BanDto(
                        int championId,
                        int pickTurn) {
        }

        public static enum RoleDto {
                NONE,
                DUO,
                SOLO,
                CARRY,
                SUPPORT
        }

        public static record PerksDto(
                        PerkStatsDto statPerks,
                        PerkStyleDto[] styles) {
        }

        public static record PerkStatsDto(
                        int defense,
                        int flex,
                        int offense) {
        }

        public static record PerkStyleDto(
                        String description,
                        List<PerkStyleSelectionDto> selections,
                        int style) {
        }

        public static record PerkStyleSelectionDto(
                        int perk,
                        int var1,
                        int var2,
                        int var3) {
        }

        public static enum LaneDto {
                TOP,
                JUNGLE,
                MIDDLE,
                BOTTOM;
        }

}