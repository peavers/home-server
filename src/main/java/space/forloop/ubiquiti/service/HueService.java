package space.forloop.ubiquiti.service;

import org.springframework.stereotype.Service;
import space.forloop.hue.api.HueClient;
import space.forloop.hue.api.Light;
import space.forloop.hue.exception.HueException;
import space.forloop.hue.model.BridgeAuthentication;
import space.forloop.hue.model.LightState;

import java.util.Optional;

@Service
public class HueService {

    public void turnAllLightsOn() {

        BridgeAuthentication authentication = new BridgeAuthentication("nlKPA666DY41rnC3w-59nT6T9U36y9Y9u9G3uY5x", null);

        // Create an instance of the client
        HueClient client = HueClient.builder().build();

        // Find a bridge on your network
        client.discoverBridges().stream().findFirst().ifPresent(bridge -> {

            // authenticate with the bridge
            var authBridge = bridge.authenticate(authentication);

            // Do something with all the lights we can find
            try {
                for (Light light : authBridge.getLights()) {
                    log.info("Changing light: {}", light.getId());
                    light.setState(LightState
                            .builder()
                            .on(true)
                            .brightness(Optional.of(254))
                            .build());
                }
            } catch (HueException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
