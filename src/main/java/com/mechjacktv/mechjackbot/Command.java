package com.mechjacktv.mechjackbot;

public interface Command {

    String getCommandTrigger();

    boolean isHandledMessage(MessageEvent messageEvent);

    void handleMessage(MessageEvent messageEvent);

}
