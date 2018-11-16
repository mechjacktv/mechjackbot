package com.mechjacktv.mechjackbot;

public interface CommandUtils {

  boolean hasRole(Command command, MessageEvent messageEvent);

  boolean isCooledDown(Command command, MessageEvent messageEvent);

  boolean isTriggered(Command command, MessageEvent messageEvent);

  void sendUsage(Command command, MessageEvent messageEvent);

  Message messageWithoutTrigger(Command command, MessageEvent messageEvent);

  ChatUsername sanitizedChatUsername(Command command, MessageEvent messageEvent);

}
