package space.forloop.ubiquiti.controller;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import space.forloop.hue.api.HueBridge;
import space.forloop.ubiquiti.model.ubiquiti.AlarmEvent;
import space.forloop.ubiquiti.service.HueBridgeService;
import space.forloop.ubiquiti.service.HueService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class EventController {

    private final HueBridgeService hueBridgeService;

    private final HueService hueService;

    // ScheduledExecutorService for handling delayed tasks
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    // Holds the scheduled task for turning off the lights
    private ScheduledFuture<?> scheduledFuture;

    // Lock object for synchronizing access to scheduledFuture
    private final Object lock = new Object();

    @PostMapping("/event")
    public void event(@RequestBody final AlarmEvent alarmEvent) {
        log.info("Event: {}", alarmEvent);

        hueBridgeService.findFirstBridge().ifPresent(bridge -> {
            // Turn on all lights
            hueService.findLights(bridge).forEach(hueService::turnOnLight);

            // Schedule turning off the lights after 30 seconds
            scheduleTurnOff(bridge);
        });
    }

    /**
     * Schedules a task to turn off the lights after 30 seconds.
     * If a task is already scheduled, it cancels it and resets the timer.
     *
     * @param bridge the Hue bridge whose lights will be turned off
     */
    private void scheduleTurnOff(HueBridge bridge) {
        synchronized (lock) {
            // Cancel any existing scheduled task
            if (scheduledFuture != null && !scheduledFuture.isDone()) {
                scheduledFuture.cancel(false);
                log.info("Existing turn-off task canceled. Resetting timer.");
            }

            // Schedule a new task to turn off the lights after 30 seconds
            scheduledFuture = scheduler.schedule(() -> {
                hueService.findLights(bridge).forEach(hueService::turnOffLight);
                log.info("Lights turned off after 30 seconds.");
            }, 30, TimeUnit.SECONDS);

            log.info("Scheduled a new turn-off task for 30 seconds later.");
        }
    }

    /**
     * Shuts down the scheduler when the application is stopping.
     */
    @PreDestroy
    public void shutdown() {
        scheduler.shutdownNow();
        log.info("Scheduler shut down.");
    }
}