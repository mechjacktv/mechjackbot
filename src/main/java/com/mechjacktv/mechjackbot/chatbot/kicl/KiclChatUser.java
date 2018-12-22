package com.mechjacktv.mechjackbot.chatbot.kicl;

import com.mechjacktv.mechjackbot.ChatBotConfiguration;
import com.mechjacktv.mechjackbot.ChatUser;
import com.mechjacktv.mechjackbot.UserRole;
import com.mechjacktv.twitchclient.TwitchLogin;

import org.kitteh.irc.client.library.element.User;

public class KiclChatUser implements ChatUser {

  private final ChatBotConfiguration chatBotConfiguration;
  private final User user;

  KiclChatUser(final ChatBotConfiguration chatBotConfiguration, final User user) {
    this.chatBotConfiguration = chatBotConfiguration;
    this.user = user;
  }

  @Override
  public TwitchLogin getTwitchLogin() {
    return TwitchLogin.of(this.user.getNick());
  }

  @Override
  public boolean hasAccessLevel(UserRole userRole) {
    return this.chatBotConfiguration.getChatChannel().value.equalsIgnoreCase(this.getTwitchLogin().value);
  }

}
