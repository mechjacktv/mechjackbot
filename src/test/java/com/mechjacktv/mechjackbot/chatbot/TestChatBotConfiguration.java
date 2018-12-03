package com.mechjacktv.mechjackbot.chatbot;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.testframework.ArbitraryDataGenerator;
import com.mechjacktv.twitchclient.TwitchLogin;

public class TestChatBotConfiguration implements ChatBotConfiguration {

  private final DataLocation dataLocation;
  private final TwitchChannel twitchChannel;
  private final TwitchPassword twitchPassword;
  private final TwitchLogin twitchLogin;

  @Inject
  public TestChatBotConfiguration(final ArbitraryDataGenerator arbitraryDataGenerator) {
    this.dataLocation = DataLocation.of(arbitraryDataGenerator.getString());
    this.twitchChannel = TwitchChannel.of(arbitraryDataGenerator.getString());
    this.twitchPassword = TwitchPassword.of(arbitraryDataGenerator.getString());
    this.twitchLogin = TwitchLogin.of(arbitraryDataGenerator.getString());
  }

  @Override
  public DataLocation getDataLocation() {
    return this.dataLocation;
  }

  @Override
  public TwitchChannel getTwitchChannel() {
    return this.twitchChannel;
  }

  @Override
  public TwitchPassword getTwitchPassword() {
    return this.twitchPassword;
  }

  @Override
  public TwitchLogin getTwitchLogin() {
    return this.twitchLogin;
  }
}
