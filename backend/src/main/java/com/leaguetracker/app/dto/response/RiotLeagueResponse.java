package com.leaguetracker.app.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonValue;
import com.leaguetracker.app.dto.RiotLeagueEntry;

import lombok.Builder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public record RiotLeagueResponse(
                List<RiotLeagueEntry> leagues) {

        @JsonCreator
        public RiotLeagueResponse(List<RiotLeagueEntry> leagues) {
                this.leagues = leagues != null ? leagues : List.of();
        }

        @JsonValue
        public List<RiotLeagueEntry> leagues() {
                return leagues;
        }
}
