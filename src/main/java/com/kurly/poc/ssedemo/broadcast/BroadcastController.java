package com.kurly.poc.ssedemo.broadcast;

import com.kurly.poc.ssedemo.openchat.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/broadcast")
public class BroadcastController {
  private static final long DEFAULT_TIMEOUT = 1000L * 60;
  private final Logger logger = LoggerFactory.getLogger(BroadcastController.class);
  private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

  @GetMapping("/connect")
  public SseEmitter connect() {
    logger.info("/broadcast/connect");

    final SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
    emitters.add(emitter);
    emitter.onCompletion(() -> emitters.remove(emitter));
    emitter.onTimeout(() -> emitters.remove(emitter));

    return emitter;
  }

  @ResponseBody
  @PostMapping("/notify")
  public int broadcast(ChatMessage message) {
    logger.info("/broadcast/notify : {}", message.getMessage());

    int count = 0;
    for (SseEmitter emitter : emitters) {
      try {
        emitter.send(message, MediaType.APPLICATION_JSON);
        count++;
      } catch (IOException e) {
        emitter.complete();
        emitters.remove(emitter);
        e.printStackTrace();
      }
    }
    return count;
  }

}
