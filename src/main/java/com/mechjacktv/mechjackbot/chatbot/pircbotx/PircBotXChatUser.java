package com.mechjacktv.mechjackbot.chatbot.pircbotx;

import org.pircbotx.User;

import com.mechjacktv.mechjackbot.ChatBotConfiguration;
import com.mechjacktv.mechjackbot.ChatUser;
import com.mechjacktv.mechjackbot.UserRole;
import com.mechjacktv.twitchclient.TwitchLogin;

final class PircBotXChatUser implements ChatUser {

  private final ChatBotConfiguration chatBotConfiguration;
  private final User user;

  PircBotXChatUser(final ChatBotConfiguration chatBotConfiguration, final User user) {
    this.chatBotConfiguration = chatBotConfiguration;
    this.user = user;
  }

  @Override
  public TwitchLogin getTwitchLogin() {
    return TwitchLogin.of(this.user.getNick());
  }

  @Override
  public boolean hasUserRole(UserRole userRole) {
    return this.chatBotConfiguration.getChatChannel().value.equalsIgnoreCase(this.getTwitchLogin().value);
  }

}
