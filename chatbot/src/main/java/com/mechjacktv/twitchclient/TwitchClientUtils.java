package com.mechjacktv.twitchclient;

import com.mechjacktv.util.function.ConsumerWithException;

import java.io.Reader;

public interface TwitchClientUtils {

  String TWITCH_API_URL = "https://api.twitch.tv/helix";

  void handleInvalidObjectName(String name);

  void handleResponse(TwitchUrl serviceUrl, ConsumerWithException<Reader> consumer);

}
