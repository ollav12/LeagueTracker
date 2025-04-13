package com.leaguetracker.app.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Builder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public record RiotMatchListResponse(
                List<String> matchIds) {
        @JsonCreator
        public RiotMatchListResponse(List<String> matchIds) {
                this.matchIds = matchIds != null ? matchIds : List.of();
        }

        @JsonValue
        public List<String> matchIds() {
                return matchIds;
        }
}
