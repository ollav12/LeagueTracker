package com.leaguetracker.app.Exception;

public class LeagueNotFoundException extends RuntimeException {

    public LeagueNotFoundException(String message) {
        super(message);
    }

    public LeagueNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
