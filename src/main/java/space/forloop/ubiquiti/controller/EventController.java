package space.forloop.ubiquiti.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import space.forloop.ubiquiti.model.ubiquiti.AlarmEvent;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EventController {

    @PostMapping
    public void event(@RequestBody final AlarmEvent alarmEvent) {
        log.info("Event: {}", alarmEvent);
    }

}
