package com.mechjacktv.mechjackbot.chatbot.kicl;

import javax.inject.Inject;

import org.kitteh.irc.client.library.element.User;

import com.mechjacktv.mechjackbot.ChatBotConfiguration;
import com.mechjacktv.mechjackbot.ChatUser;
import com.mechjacktv.mechjackbot.chatbot.ChatUserFactory;

public class KiclChatUserFactory implements ChatUserFactory<User> {

  private final ChatBotConfiguration chatBotConfiguration;

  @Inject
  KiclChatUserFactory(final ChatBotConfiguration chatBotConfiguration) {
    this.chatBotConfiguration = chatBotConfiguration;
  }

  @Override
  public ChatUser create(final User user) {
    return new KiclChatUser(this.chatBotConfiguration, user);
  }

}
