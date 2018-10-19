package com.mechjacktv.mechjackbot;

public interface Command {

    String getDescription();

    String getName();

    String getTrigger();

    boolean isHandledMessage(MessageEvent messageEvent);

    boolean isListed();

    void handleMessage(MessageEvent messageEvent);

}
