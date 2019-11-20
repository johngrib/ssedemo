package com.kurly.poc.ssedemo.openchat;

import com.kurly.poc.ssedemo.common.Message;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/chat")
public class ChatController {
  private static final long DEFAULT_TIMEOUT = 1000L * 60;
  private final Logger logger = LoggerFactory.getLogger(ChatController.class);
  private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

  /**
   * 채팅 참여자를 받는다.
   */
  @GetMapping("/connect")
  public SseEmitter connect() {
    logger.info("/connect");

    final SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
    emitters.add(emitter);
    emitter.onCompletion(() -> emitters.remove(emitter));
    emitter.onTimeout(() -> emitters.remove(emitter));

    return emitter;
  }

  /**
   * 채팅 참여자들에게 메시지를 전달한다.
   *
   * @param message 전달할 메시지
   * @return 전달한 메시지
   */
  @ResponseBody
  @PostMapping("/send")
  public Message broadcast(ChatMessage message) {
    logger.info("/send: received message" + message);

    emitters.forEach((SseEmitter emitter) -> {
      try {
        emitter.send(message, MediaType.APPLICATION_JSON);
      } catch (IOException e) {
        emitter.complete();
        emitters.remove(emitter);
        e.printStackTrace();
      }
    });
    return message;
  }

}
