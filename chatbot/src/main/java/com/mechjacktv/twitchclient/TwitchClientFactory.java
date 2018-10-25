package com.mechjacktv.twitchclient;

public interface TwitchClientFactory {

  TwitchClient createTwitchClient(TwitchClientId clientId);

}
