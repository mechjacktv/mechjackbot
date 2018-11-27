package com.mechjacktv.mechjackbot;

import com.mechjacktv.twitchclient.TwitchLogin;

public interface ChatBotConfiguration {

  DataLocation getDataLocation();

  TwitchChannel getTwitchChannel();

  TwitchPassword getTwitchPassword();

  TwitchLogin getTwitchLogin();

}
