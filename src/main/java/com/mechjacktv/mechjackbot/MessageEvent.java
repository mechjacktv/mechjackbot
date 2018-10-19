package com.mechjacktv.mechjackbot;

public interface MessageEvent {

    ChatBot getChatBot();

    ChatUser getChatUser();

    String getMessage();

    void sendResponse(String message);

}
