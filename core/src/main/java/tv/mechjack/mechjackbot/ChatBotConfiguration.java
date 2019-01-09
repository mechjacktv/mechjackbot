package tv.mechjack.mechjackbot;

import tv.mechjack.twitchclient.TwitchLogin;

public interface ChatBotConfiguration {

  ChatChannel getChatChannel();

  UserPassword getUserPassword();

  TwitchLogin getTwitchLogin();

}
