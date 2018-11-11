package com.mechjacktv.twitchclient;

import org.slf4j.Logger;

import com.mechjacktv.util.ExecutionUtils;

public class DefaultTwitchClientUtilsUnitTests extends TwitchClientUtilsContractTests {

  @Override
  TwitchClientUtils givenASubjectToTest(final TwitchClientConfiguration twitchClientConfiguration,
      final ExecutionUtils executionUtils, final UrlConnectionFactory urlConnectionFactory, final Logger logger) {
    return new DefaultTwitchClientUtils(twitchClientConfiguration, executionUtils, urlConnectionFactory, logger);
  }

}
