package com.mechjacktv.mechjackbot;

import com.mechjacktv.twitchclient.TwitchLogin;

public interface ChatUser {

  TwitchLogin getTwitchLogin();

  boolean hasUserRole(UserRole userRole);

}
