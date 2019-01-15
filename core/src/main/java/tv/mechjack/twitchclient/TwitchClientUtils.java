package tv.mechjack.twitchclient;

import java.io.Reader;

import tv.mechjack.platform.util.function.ConsumerWithException;

public interface TwitchClientUtils {

  String TWITCH_API_URL = "https://api.twitch.tv/helix";

  void handleUnknownObjectName(String name);

  void handleResponse(TwitchUrl serviceUrl, ConsumerWithException<Reader> consumer);

}
