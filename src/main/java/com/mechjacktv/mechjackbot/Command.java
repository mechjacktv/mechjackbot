package com.mechjacktv.mechjackbot;

public interface Command {

    String getCommandTrigger();

    String getDescription();

    boolean isHandledMessage(MessageEvent messageEvent);

    boolean isListed();

    void handleMessage(MessageEvent messageEvent);

}
