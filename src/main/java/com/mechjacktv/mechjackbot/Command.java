package com.mechjacktv.mechjackbot;

public interface Command {

    boolean isHandledMessage(MessageEvent messageEvent);

    void handleMessage(MessageEvent messageEvent);

}
