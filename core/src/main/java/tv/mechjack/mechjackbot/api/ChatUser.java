package tv.mechjack.mechjackbot.api;

import tv.mechjack.twitchclient.TwitchLogin;

public interface ChatUser {

  TwitchLogin getTwitchLogin();

  boolean hasUserRole(UserRole userRole);

}
