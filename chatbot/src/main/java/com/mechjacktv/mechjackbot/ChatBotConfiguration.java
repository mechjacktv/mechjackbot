package com.mechjacktv.mechjackbot;

import com.mechjacktv.twitchclient.TwitchClientId;

public interface ChatBotConfiguration {

  DataLocation getDataLocation();

  TwitchChannel getTwitchChannel();

  TwitchClientId getTwitchClientId();

  TwitchPassword getTwitchPassword();

  TwitchUsername getTwitchUsername();

}
