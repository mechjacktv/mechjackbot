package tv.mechjack.twitchclient;

import javax.inject.Inject;

import tv.mechjack.testframework.ArbitraryData;

public class TestTwitchClientConfiguration implements TwitchClientConfiguration {

  private final TwitchClientId twitchClientId;

  @Inject
  TestTwitchClientConfiguration(final ArbitraryData arbitraryDataGenerator) {
    this.twitchClientId = TwitchClientId.of(arbitraryDataGenerator.getString());
  }

  @Override
  public TwitchClientId getTwitchClientId() {
    return this.twitchClientId;
  }

}
