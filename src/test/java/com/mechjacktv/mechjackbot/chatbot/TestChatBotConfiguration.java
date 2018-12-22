package com.mechjacktv.mechjackbot.chatbot;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.testframework.ArbitraryDataGenerator;
import com.mechjacktv.twitchclient.TwitchLogin;

public class TestChatBotConfiguration implements ChatBotConfiguration {

  private final DataLocation dataLocation;
  private final ChatChannel chatChannel;
  private final UserPassword userPassword;
  private final TwitchLogin twitchLogin;

  @Inject
  public TestChatBotConfiguration(final ArbitraryDataGenerator arbitraryDataGenerator) {
    this.dataLocation = DataLocation.of(arbitraryDataGenerator.getString());
    this.chatChannel = ChatChannel.of(arbitraryDataGenerator.getString());
    this.userPassword = UserPassword.of(arbitraryDataGenerator.getString());
    this.twitchLogin = TwitchLogin.of(arbitraryDataGenerator.getString());
  }

  @Override
  public DataLocation getDataLocation() {
    return this.dataLocation;
  }

  @Override
  public ChatChannel getChatChannel() {
    return this.chatChannel;
  }

  @Override
  public UserPassword getUserPassword() {
    return this.userPassword;
  }

  @Override
  public TwitchLogin getTwitchLogin() {
    return this.twitchLogin;
  }
}
