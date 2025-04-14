package com.leaguetracker.app.exception;

public class LeagueNotFoundException extends RuntimeException {

    public LeagueNotFoundException(String message) {
        super(message);
    }

    public LeagueNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
