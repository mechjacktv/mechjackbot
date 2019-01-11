package tv.mechjack.mechjackbot.api;

import tv.mechjack.twitchclient.TwitchLogin;

public interface ChatBotConfiguration {

  ChatChannel getChatChannel();

  UserPassword getUserPassword();

  TwitchLogin getTwitchLogin();

}
