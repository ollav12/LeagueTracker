package com.leaguetracker.app.mapper;

import java.util.function.Function;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leaguetracker.app.dto.AccountDto;

public class AccountMapper implements Function<String, AccountDto> {

    private final ObjectMapper objectMapper;

    public AccountMapper() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public AccountDto apply(String response) {
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            AccountDto accountDto = new AccountDto(jsonNode.get("puuid").asText(), jsonNode.get("gameName").asText(),
                    jsonNode.get("tagLine").asText());
            return accountDto;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse response", e);
        }
    }

}
