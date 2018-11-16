package com.mechjacktv.mechjackbot.configuration;

import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.util.ArbitraryDataGenerator;

public class ArbitraryChatBotConfiguration implements ChatBotConfiguration {

  private final DataLocation dataLocation;
  private final TwitchChannel twitchChannel;
  private final TwitchPassword twitchPassword;
  private final TwitchUsername twitchUsername;

  public ArbitraryChatBotConfiguration() {
    this(new ArbitraryDataGenerator());
  }

  public ArbitraryChatBotConfiguration(final ArbitraryDataGenerator arbitraryDataGenerator) {
    this.dataLocation = DataLocation.of(arbitraryDataGenerator.getString());
    this.twitchChannel = TwitchChannel.of(arbitraryDataGenerator.getString());
    this.twitchPassword = TwitchPassword.of(arbitraryDataGenerator.getString());
    this.twitchUsername = TwitchUsername.of(arbitraryDataGenerator.getString());
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
  public TwitchUsername getTwitchUsername() {
    return this.twitchUsername;
  }
}