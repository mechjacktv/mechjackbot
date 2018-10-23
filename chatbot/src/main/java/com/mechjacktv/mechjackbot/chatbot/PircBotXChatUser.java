package com.mechjacktv.mechjackbot.chatbot;

import com.mechjacktv.mechjackbot.ChatUser;
import org.pircbotx.User;

final class PircBotXChatUser implements ChatUser {

  private final User user;

  PircBotXChatUser(final User user) {
    this.user = user;
  }

  @Override
  public String getUsername() {
    return this.user.getNick();
  }

}
