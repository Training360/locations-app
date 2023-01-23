package locationsapp.rest;

import locationsapp.service.LocationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
public class LocationsSseController {

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @GetMapping("/events")
    public SseEmitter getMessages() {
        SseEmitter emitter = new SseEmitter();
        emitters.add(emitter);
        return emitter;
    }

    @EventListener
    public void employeeHasCreated(LocationEvent event) {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        this.emitters.forEach(emitter -> {
            try {
                SseEmitter.SseEventBuilder builder = SseEmitter.event()
                        .name("message")
                        .comment("Location event")
                        .id(UUID.randomUUID().toString())
                        .reconnectTime(10_000)
                        .data(event);
                emitter.send(builder);
            }
            catch (Exception e) {
                deadEmitters.add(emitter);
            }
        });

        this.emitters.removeAll(deadEmitters);
    }


}
