package tv.mechjack.mechjackbot.api;

import tv.mechjack.twitchclient.TwitchLogin;

public interface ChatBotConfiguration {

  boolean isReady();

  ChatChannel getChatChannel();

  UserPassword getUserPassword();

  TwitchLogin getTwitchLogin();

}
