package com.mechjacktv.mechjackbot.chatbot;

import com.mechjacktv.mechjackbot.ChatUser;

@FunctionalInterface
public interface ChatUserFactory<U> {

  ChatUser create(U user);

}
