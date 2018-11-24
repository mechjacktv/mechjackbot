package com.mechjacktv.mechjackbot.chatbot;

import com.mechjacktv.mechjackbot.ChatUser;

@FunctionalInterface
interface ChatUserFactory<U> {

  ChatUser create(U user);

}
