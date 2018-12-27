package tv.mechjack.mechjackbot;

import tv.mechjack.twitchclient.TwitchLogin;

public interface ChatBotConfiguration {

  DataLocation getDataLocation();

  ChatChannel getChatChannel();

  UserPassword getUserPassword();

  TwitchLogin getTwitchLogin();

}
