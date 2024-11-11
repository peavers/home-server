package space.forloop.ubiquiti.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import space.forloop.hue.api.HueClient;
import space.forloop.hue.model.BridgeAuthentication;

@Configuration
public class HueConfig {

    @Bean
    BridgeAuthentication bridgeAuthentication() {
        return new BridgeAuthentication("nlKPA666DY41rnC3w-59nT6T9U36y9Y9u9G3uY5x", null);
    }

    @Bean
    HueClient getClient() {
        return HueClient.builder().build();
    }
}
