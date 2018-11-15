package com.mechjacktv.mechjackbot;

public interface Command {

  CommandName getName();

  CommandDescription getDescription();

  boolean isViewerTriggerable();

  CommandTrigger getTrigger();

  boolean isTriggered(MessageEvent messageEvent);

  void handleMessageEvent(MessageEvent messageEvent);

}
