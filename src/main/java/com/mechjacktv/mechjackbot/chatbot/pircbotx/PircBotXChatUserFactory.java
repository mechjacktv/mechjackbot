package com.mechjacktv.mechjackbot.chatbot.pircbotx;

import javax.inject.Inject;

import org.pircbotx.User;

import com.mechjacktv.mechjackbot.ChatBotConfiguration;
import com.mechjacktv.mechjackbot.ChatUser;
import com.mechjacktv.mechjackbot.chatbot.ChatUserFactory;

class PircBotXChatUserFactory implements ChatUserFactory<User> {

  private final ChatBotConfiguration chatBotConfiguration;

  @Inject
  PircBotXChatUserFactory(final ChatBotConfiguration chatBotConfiguration) {
    this.chatBotConfiguration = chatBotConfiguration;
  }

  @Override
  public ChatUser create(final User user) {
    return new PircBotXChatUser(this.chatBotConfiguration, user);
  }
}
