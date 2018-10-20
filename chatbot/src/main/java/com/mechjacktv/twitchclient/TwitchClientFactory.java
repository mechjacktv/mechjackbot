package com.mechjacktv.twitchclient;

public interface TwitchClientFactory {

    TwitchClientBuilder newBuilder();

    TwitchClient createTwitchClient(String clientId);

}
