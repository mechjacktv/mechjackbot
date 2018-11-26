package com.mechjacktv.mechjackbot.chatbot;

import com.mechjacktv.mechjackbot.ChatBot;

@FunctionalInterface
interface ChatBotFactory<B> {

  ChatBot create(B bot);

}
