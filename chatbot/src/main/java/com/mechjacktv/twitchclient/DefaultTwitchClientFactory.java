package com.mechjacktv.twitchclient;

import com.google.gson.Gson;
import com.mechjacktv.util.ExecutionUtils;

import javax.inject.Inject;

final class DefaultTwitchClientFactory implements TwitchClientFactory {

  private final ExecutionUtils executionUtils;
  private final Gson gson;

  @Inject
  DefaultTwitchClientFactory(final ExecutionUtils executionUtils, final Gson gson) {
    this.executionUtils = executionUtils;
    this.gson = gson;
  }

  @Override
  public TwitchClient createTwitchClient(TwitchClientId clientId) {
    final TwitchClientUtils twitchClientUtils = new TwitchClientUtils(clientId, this.executionUtils);

    return new DefaultTwitchClient(this.gson, twitchClientUtils);
  }

}
