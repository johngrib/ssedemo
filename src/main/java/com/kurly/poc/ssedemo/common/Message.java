package com.kurly.poc.ssedemo.common;

import java.time.LocalDateTime;

public interface Message {
  public String getSender();
  public String getReceiver();
  public String getMessage();
  public LocalDateTime getTime();
}
