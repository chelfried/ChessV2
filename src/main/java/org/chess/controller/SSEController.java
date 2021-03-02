package org.chess.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
public class SSEController {

    private static final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @CrossOrigin
    @RequestMapping("api/subscribe")
    public static SseEmitter subscribeToSSE() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitters.add(emitter);
        return emitter;
    }

    @PostMapping("api/event")
    public static void refreshPage() {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(emitter);
            } catch (IOException e) {
                emitters.remove(emitter);
            }
        }
    }

    private SSEController() {
    }
}
