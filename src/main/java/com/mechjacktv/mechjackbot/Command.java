package com.mechjacktv.mechjackbot;

public interface Command {

  CommandName getName();

  CommandDescription getDescription();

  CommandUsage getUsage();

  boolean isTriggerable();

  CommandTrigger getTrigger();

  boolean isTriggered(MessageEvent messageEvent);

  void handleMessageEvent(MessageEvent messageEvent);

}
