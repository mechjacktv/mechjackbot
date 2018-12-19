package com.mechjacktv.twitchclient;

import javax.inject.Inject;

import com.mechjacktv.testframework.ArbitraryDataGenerator;

public class TestTwitchClientConfiguration implements TwitchClientConfiguration {

  private final TwitchClientId twitchClientId;

  @Inject
  TestTwitchClientConfiguration(final ArbitraryDataGenerator arbitraryDataGenerator) {
    this.twitchClientId = TwitchClientId.of(arbitraryDataGenerator.getString());
  }

  @Override
  public TwitchClientId getTwitchClientId() {
    return this.twitchClientId;
  }

}
