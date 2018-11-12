package com.mechjacktv.twitchclient;

public class DefaultTwitchClientUnitTests extends TwitchClientContractTests {

  @Override
  TwitchClient givenASubjectToTest(final TwitchUsersEndpoint twitchUsersEndpoint,
      final TwitchUsersFollowsEndpoint twitchUsersFollowsEndpoint) {
    return new DefaultTwitchClient(twitchUsersEndpoint, twitchUsersFollowsEndpoint);
  }
}
