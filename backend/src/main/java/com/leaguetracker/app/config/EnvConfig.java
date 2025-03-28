package com.leaguetracker.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.github.cdimascio.dotenv.Dotenv;

@Configuration
public class EnvConfig {

    private final Dotenv dotenv = Dotenv.load();

    @Bean
    public String getApiKey() {
        return dotenv.get("RIOT_KEY");
    }
}
