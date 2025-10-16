package com.leaguetracker.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.leaguetracker.app.config.TestWebClientConfig;
import com.leaguetracker.app.config.WebClientConfig;
import com.leaguetracker.app.dto.response.RiotAccountResponse;
import com.leaguetracker.app.helper.Helper;
import com.leaguetracker.app.service.riot.RiotRequest;
import com.leaguetracker.app.service.riot.endpoint.RiotEndpoint;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class RiotRequestTest {

    private WireMockServer wireMockServer;
    private ObjectMapper objectMapper;
    private WebClient webClient;
    private WebClientConfig webClientConfig;

    @BeforeEach
    void setup() {
        wireMockServer = new WireMockServer(8099);
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());

        objectMapper = new ObjectMapper();
        webClient = WebClient.builder().baseUrl("http://localhost:" + wireMockServer.port()).build();
        webClientConfig = new TestWebClientConfig();
    }

    @AfterEach
    void teardown() {
        wireMockServer.stop();
    }

    @Test
    void testRateLimitRetry() {
        // Stub 429 for the exact path the request will use (relative path)
        wireMockServer.stubFor(get(urlPathEqualTo("/riot/account/v1/accounts/by-riot-id/test123/euw"))
                .willReturn(aResponse()
                        .withStatus(429)
                        .withHeader("Retry-After", "0")
                        .withBody("{\"error\":\"rate limit exceeded\"}")));

        RiotRequest<RiotAccountResponse> request = new RiotRequest<>(
                RiotEndpoint.ACCOUNT_BY_RIOT_ID,
                Helper.getRiotApiRegion("euw1"),
                objectMapper,
                RiotAccountResponse.class,
                webClientConfig,
                webClient,
                "test123",
                "euw"
        );

        try {
            request.execute();
        } catch (Exception ignored) {
            // Expected after retries
        }

        // Initial call + 3 retries = 4 total
        wireMockServer.verify(4, getRequestedFor(urlPathEqualTo("/riot/account/v1/accounts/by-riot-id/test123/euw")));
    }
}