package com.mechjacktv.mechjackbot;

public interface ChatMessageEvent {

  ChatBot getChatBot();

  ChatUser getChatUser();

  ChatMessage getChatMessage();

  void sendResponse(ChatMessage chatMessage);

}
