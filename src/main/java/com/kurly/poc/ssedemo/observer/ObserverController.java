package com.kurly.poc.ssedemo.observer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.catalina.connector.ClientAbortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/observer")
public class ObserverController {
  private static final long DEFAULT_TIMEOUT = 1000L * 60;
  private final Logger logger = LoggerFactory.getLogger(ObserverController.class);
  private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

  /**
   * 클라이언트의 구독 요청을 받는다.
   */
  @GetMapping("/connect")
  public SseEmitter subscribe() {
    logger.info("/connect");

    final SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
    emitter.onCompletion(() -> {
      emitters.remove(emitter);
      broadcast();
    });
    emitter.onTimeout(() -> {
      emitters.remove(emitter);
      broadcast();
    });
    emitters.add(emitter);
    broadcast();

    return emitter;
  }

  private void broadcast() {
    logger.info("broadcast");
    emitters.forEach((SseEmitter emitter) -> {
      try {
        emitter.send(SseEmitter.event().data(emitters.size()));
      } catch (IllegalStateException | ClientAbortException e) {
        // timeout or completion state
        logger.warn("unstable event stream connection (reason: {})", e.getMessage());
        emitters.remove(emitter);
      } catch (Exception e) {
        logger.error("failed to broadcast event to emitter (reason: " + e.getMessage() + ")");
        emitters.remove(emitter);
      }
    });
  }
}

