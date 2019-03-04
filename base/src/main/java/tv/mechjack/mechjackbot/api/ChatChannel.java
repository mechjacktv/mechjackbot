package tv.mechjack.mechjackbot.api;

import java.util.List;

import tv.mechjack.twitchclient.TwitchLogin;

public interface ChatChannel {

  List<TwitchLogin> getTwitchLogins();

}
