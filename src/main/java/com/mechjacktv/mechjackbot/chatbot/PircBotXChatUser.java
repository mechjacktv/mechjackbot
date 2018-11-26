package com.mechjacktv.mechjackbot.chatbot;

import org.pircbotx.User;

import com.mechjacktv.mechjackbot.*;

final class PircBotXChatUser implements ChatUser {

  private final ChatBotConfiguration chatBotConfiguration;
  private final CommandUtils commandUtils;
  private final User user;

  PircBotXChatUser(final ChatBotConfiguration chatBotConfiguration, final CommandUtils commandUtils, final User user) {
    this.chatBotConfiguration = chatBotConfiguration;
    this.commandUtils = commandUtils;
    this.user = user;
  }

  @Override
  public ChatUsername getUsername() {
    return ChatUsername.of(this.user.getNick());
  }

  @Override
  public boolean hasAccessLevel(AccessLevel accessLevel) {
    return this.chatBotConfiguration.getTwitchChannel().value
        .equalsIgnoreCase(this.commandUtils.sanitizeChatUsername(this.getUsername()).value);
  }

}
