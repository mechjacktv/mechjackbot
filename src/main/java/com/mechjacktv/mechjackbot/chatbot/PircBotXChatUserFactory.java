package com.mechjacktv.mechjackbot.chatbot;

import org.pircbotx.User;

import com.mechjacktv.mechjackbot.ChatBotConfiguration;
import com.mechjacktv.mechjackbot.ChatUser;
import com.mechjacktv.mechjackbot.CommandUtils;

class PircBotXChatUserFactory implements ChatUserFactory<User> {

  private final ChatBotConfiguration chatBotConfiguration;
  private final CommandUtils commandUtils;

  PircBotXChatUserFactory(final ChatBotConfiguration chatBotConfiguration, final CommandUtils commandUtils) {
    this.chatBotConfiguration = chatBotConfiguration;
    this.commandUtils = commandUtils;
  }

  @Override
  public ChatUser create(final User user) {
    return new PircBotXChatUser(this.chatBotConfiguration, this.commandUtils, user);
  }
}
