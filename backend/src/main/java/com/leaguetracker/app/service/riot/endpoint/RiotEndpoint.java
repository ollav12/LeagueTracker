package com.leaguetracker.app.service.riot.endpoint;

import com.leaguetracker.app.exception.*;

public enum RiotEndpoint {
    ACCOUNT_BY_PUUID("riot/account/v1/accounts/by-puuid/%s") {
        @Override
        public RuntimeException mapError(int statusCode, String message) {
            return switch (statusCode) {
                case 400 -> new InvalidRequestException(message);
                case 404 -> new AccountNotFoundException(message);
                default -> new RuntimeException("API error: " + message + ", status: " + statusCode);
            };
        }
    },
    ACCOUNT_BY_RIOT_ID("riot/account/v1/accounts/by-riot-id/%s/%s") {
        @Override
        public RuntimeException mapError(int statusCode, String message) {
            return switch (statusCode) {
                case 400 -> new InvalidRequestException(message);
                case 404 -> new AccountNotFoundException(message);
                default -> new RuntimeException("API error: " + message + ", status: " + statusCode);
            };
        }
    },
    SUMMONER_BY_PUUID("lol/summoner/v4/summoners/by-puuid/%s") {
        @Override
        public RuntimeException mapError(int statusCode, String message) {
            return switch (statusCode) {
                case 400 -> new InvalidRequestException(message);
                case 404 -> new SummonerNotFoundException(message);
                default -> new RuntimeException("API error: " + message + ", status: " + statusCode);
            };
        }
    },
    LEAGUE_BY_SUMMONER("lol/league/v4/entries/by-summoner/%s") {
        @Override
        public RuntimeException mapError(int statusCode, String message) {
            return switch (statusCode) {
                case 400 -> new InvalidRequestException(message);
                case 404 -> new LeagueNotFoundException(message);
                default -> new RuntimeException("API error: " + message + ", status: " + statusCode);
            };
        }
    },
    LEAGUE_BY_PUUID("lol/league/v4/entries/by-puuid/%s") {
        @Override
        public RuntimeException mapError(int statusCode, String message) {
            return switch (statusCode) {
                case 400 -> new InvalidRequestException(message);
                case 404 -> new LeagueNotFoundException(message);
                default -> new RuntimeException("API error: " + message + ", status: " + statusCode);
            };
        }
    },
    MATCH_BY_ID("lol/match/v5/matches/%s") {
        @Override
        public RuntimeException mapError(int statusCode, String message) {
            return switch (statusCode) {
                case 400 -> new InvalidRequestException(message);
                case 404 -> new MatchNotFoundException(message);
                default -> new RuntimeException("API error: " + message + ", status: " + statusCode);
            };
        }
    },
    MATCH_LIST_BY_PUUID("lol/match/v5/matches/by-puuid/%s/ids?start=%d&count=%d") {
        @Override
        public RuntimeException mapError(int statusCode, String message) {
            return switch (statusCode) {
                case 400 -> new InvalidRequestException(message);
                case 404 -> new MatchNotFoundException(message);
                default -> new RuntimeException("API error: " + message + ", status: " + statusCode);
            };
        }
    };

    private final String pathTemplate;

    RiotEndpoint(String pathTemplate) {
        this.pathTemplate = pathTemplate;
    }

    public String getPathTemplate() {
        return pathTemplate;
    }

    public String format(Object... params) {
        return String.format(pathTemplate, params);
    }

    public abstract RuntimeException mapError(int statusCode, String message);
}
