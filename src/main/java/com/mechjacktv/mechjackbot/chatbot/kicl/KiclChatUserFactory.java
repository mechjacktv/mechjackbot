package com.mechjacktv.mechjackbot.chatbot.kicl;

import javax.inject.Inject;

import org.kitteh.irc.client.library.element.User;

import com.mechjacktv.mechjackbot.ChatBotConfiguration;
import com.mechjacktv.mechjackbot.ChatUser;

public class KiclChatUserFactory {

  private final ChatBotConfiguration chatBotConfiguration;

  @Inject
  KiclChatUserFactory(final ChatBotConfiguration chatBotConfiguration) {
    this.chatBotConfiguration = chatBotConfiguration;
  }

  public ChatUser create(final User user) {
    return new KiclChatUser(this.chatBotConfiguration, user);
  }

}
