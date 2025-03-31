package com.leaguetracker.app.service.riot;

import java.util.function.Function;

import org.springframework.web.reactive.function.client.WebClient;

public class RiotRequest<T> {

    private final WebClient webClient;
    private final String url;
    private final Function<String, T> responseMapper;

    public RiotRequest(String region, String endpoint, String apiKey, Function<String, T> responseMapper) {
        this.url = "https://" + region + ".api.riotgames.com/" + endpoint + apiKey;
        this.webClient = WebClient.create();
        this.responseMapper = responseMapper;
    }

    public T execute() {
        String response = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        System.out.println("Response from riot:" + response);
        return responseMapper.apply(response);
    }
}
