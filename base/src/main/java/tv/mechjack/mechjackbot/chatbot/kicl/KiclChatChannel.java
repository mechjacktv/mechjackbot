package tv.mechjack.mechjackbot.chatbot.kicl;

import java.util.ArrayList;
import java.util.List;

import org.kitteh.irc.client.library.element.Channel;

import tv.mechjack.mechjackbot.api.ChatChannel;
import tv.mechjack.twitchclient.TwitchLogin;

public class KiclChatChannel implements ChatChannel {

  private final Channel channel;

  KiclChatChannel(final Channel channel) {
    this.channel = channel;
  }

  @Override
  public List<TwitchLogin> getTwitchLogins() {
    final List<TwitchLogin> twitchLogins = new ArrayList<>();

    for (final String nickname : this.channel.getNicknames()) {
      twitchLogins.add(TwitchLogin.of(nickname));
    }
    return twitchLogins;
  }

}
