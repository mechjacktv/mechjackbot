package com.mechjacktv.twitchclient;

import com.mechjacktv.util.ExecutionUtils;

public class DefaultTwitchClientUtilsUnitTests extends TwitchClientUtilsContractTests {

  @Override
  TwitchClientUtils givenASubjectToTest(final TwitchClientConfiguration twitchClientConfiguration,
          final ExecutionUtils executionUtils,
          final UrlConnectionFactory urlConnectionFactory) {
    return new DefaultTwitchClientUtils(twitchClientConfiguration, executionUtils, urlConnectionFactory);
  }

}
