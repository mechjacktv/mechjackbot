package com.mechjacktv.mechjackbot;

import com.mechjacktv.twitchclient.TwitchLogin;

public interface ChatBotConfiguration {

  DataLocation getDataLocation();

  ChatChannel getChatChannel();

  UserPassword getUserPassword();

  TwitchLogin getTwitchLogin();

}
