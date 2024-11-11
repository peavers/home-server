package space.forloop.ubiquiti.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import space.forloop.hue.api.HueBridge;
import space.forloop.hue.api.HueClient;
import space.forloop.hue.api.Light;
import space.forloop.hue.exception.HueConnectionException;
import space.forloop.hue.exception.HueDiscoveryException;
import space.forloop.hue.exception.HueException;
import space.forloop.hue.model.BridgeAuthentication;
import space.forloop.hue.model.LightState;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class HueService {

    private final BridgeAuthentication bridgeAuthentication;

    public Collection<Light> findLights(HueBridge bridge) {
        var authBridge = bridge.authenticate(bridgeAuthentication);

        Collection<Light> lights;

        try {
            lights = authBridge.getLights();
        } catch (HueException e) {
            throw new RuntimeException(e);
        }

        return lights;
    }

    public void turnOnLight(Light light) {
        try {
            light.setState(LightState
                    .builder()
                    .on(true)
                    .brightness(254)
                    .build());
        } catch (HueException e) {
            throw new RuntimeException(e);
        }
    }

    public void turnOffLight(Light light) {
        try {
            light.setState(LightState
                    .builder()
                    .on(false)
                    .brightness(0)
                    .build());
        } catch (HueException e) {
            throw new RuntimeException(e);
        }
    }
}
