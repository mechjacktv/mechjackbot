package tv.mechjack.mechjackbot.chatbot.kicl;

import org.kitteh.irc.client.library.element.Channel;

import tv.mechjack.mechjackbot.api.ChatChannel;

public class KiclChatChannelFactory {

  public ChatChannel createChatChannel(
      final Channel channel) {
    channel.getLatest();
    return new KiclChatChannel(channel);
  }

}
