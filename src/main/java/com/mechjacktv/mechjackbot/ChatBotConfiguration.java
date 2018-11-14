package com.mechjacktv.mechjackbot;

public interface ChatBotConfiguration {

  DataLocation getDataLocation();

  TwitchChannel getTwitchChannel();

  TwitchPassword getTwitchPassword();

  TwitchUsername getTwitchUsername();

}
