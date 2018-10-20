package com.mechjacktv.twitchclient;

public interface TwitchClientBuilder {

    TwitchClientBuilder setClientId(String clientId);

    TwitchClient build();

}
