package com.mechjacktv.mechjackbot;

public interface MessageEvent {

    ChatBot getChatBot();

    ChatUser getChatUser();

    Message getMessage();

    void sendResponse(Message message);

}
