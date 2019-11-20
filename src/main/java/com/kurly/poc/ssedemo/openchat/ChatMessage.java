package com.kurly.poc.ssedemo.openchat;

import com.kurly.poc.ssedemo.common.Message;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ChatMessage implements Message {
  private String sender;
  private String message;
  private LocalDateTime time;

  /**
   * 대화를 위한 메시지.
   * @param sender 메시지를 보내는 사람
   * @param message 메시지 내용
   */
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
