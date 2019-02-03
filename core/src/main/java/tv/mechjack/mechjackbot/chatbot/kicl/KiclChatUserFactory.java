package tv.mechjack.mechjackbot.chatbot.kicl;

import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;

import tv.mechjack.mechjackbot.api.ChatUser;

public class KiclChatUserFactory {

  public ChatUser create(final ChannelMessageEvent event) {
    return new KiclChatUser(event);
  }

}
