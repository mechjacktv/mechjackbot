package tv.mechjack.mechjackbot.api;

import tv.mechjack.twitchclient.TwitchLogin;

public interface ChatBotConfiguration {

  ChatChannelName getChatChannelName();

  UserPassword getUserPassword();

  TwitchLogin getTwitchLogin();

}
