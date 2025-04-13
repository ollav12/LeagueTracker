package com.leaguetracker.app.service.riot.endpoint;

public enum RiotEndpoint {
    ACCOUNT_BY_PUUID("riot/account/v1/accounts/by-puuid/%s"),
    ACCOUNT_BY_RIOT_ID("riot/account/v1/accounts/by-riot-id/%s/%s"),

    SUMMONER_BY_PUUID("lol/summoner/v4/summoners/by-puuid/%s"),

    LEAGUE_BY_SUMMONER("lol/league/v4/entries/by-summoner/%s"),
    LEAGUE_BY_PUUID("lol/league/v4/entries/by-puuid/%s"),

    MATCH_BY_ID("lol/match/v5/matches/%s"),
    MATCH_LIST_BY_PUUID("lol/match/v5/matches/by-puuid/%s/ids?start=%d&count=%d");

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
}
