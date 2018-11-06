package com.mechjacktv.mechjackbot;

public interface Command {

  CommandDescription getDescription();

  CommandName getName();

  CommandTrigger getTrigger();

  void handleMessage(MessageEvent messageEvent);

  boolean isTriggerable();

  boolean isTriggered(MessageEvent messageEvent);

}
