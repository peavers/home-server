package space.forloop.ubiquiti.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import space.forloop.hue.api.HueBridge;
import space.forloop.hue.api.HueClient;
import space.forloop.hue.exception.HueConnectionException;
import space.forloop.hue.exception.HueDiscoveryException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class HueBridgeService {

    private final HueClient hueClient;

    @Cacheable("findFirstBridge")
    public Optional<HueBridge> findFirstBridge() {
        return findBridges()
                .stream()
                .findFirst();
    }

    @Cacheable("findBridges")
    public List<HueBridge> findBridges() {
        try {
            List<HueBridge> hueBridges = hueClient.discoverBridges();

            log.info("Found {} bridges", hueBridges.size());

            return hueBridges;

        } catch (HueDiscoveryException | HueConnectionException e) {
            throw new RuntimeException(e);
        }
    }
}
