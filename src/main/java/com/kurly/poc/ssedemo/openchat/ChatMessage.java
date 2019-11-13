package com.kurly.poc.ssedemo.openchat;

import com.kurly.poc.ssedemo.common.Message;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
public class ChatMessage implements Message {
  private String sender;
  private String message;
  private LocalDateTime time;

  public ChatMessage(String sender, String message) {
    this.sender = sender;
    this.message = message;
    this.time = LocalDateTime.now();
  }

  @Override
  public String getReceiver() {
    return "";
  }
}
