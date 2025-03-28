package com.leaguetracker.app.service.riot;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class RiotRequest {

    private final WebClient webClient;
    private final String url;

    public RiotRequest(String region, String endpoint, String apiKey) {
        this.url = "https://" + region + ".api.riotgames.com/" + endpoint + "?api_key=" + apiKey;
        this.webClient = WebClient.create();
    }

    public Mono<String> executeAsync() {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorReturn("Error, something went wrong with executeAsync()");
    }
}
