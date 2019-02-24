package tv.mechjack.mechjackbot.feature.linkmoderation;

import tv.mechjack.mechjackbot.api.ChatUser;
import tv.mechjack.twitchclient.TwitchLogin;

public interface LinkModeratorService {

  void grantPermit(TwitchLogin twitchLogin, long duration);

  boolean hasPermit(ChatUser chatUser);

  int timeoutDuration(ChatUser chatUser);

}
