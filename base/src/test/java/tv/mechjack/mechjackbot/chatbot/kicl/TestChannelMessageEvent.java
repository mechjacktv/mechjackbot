package tv.mechjack.mechjackbot.chatbot.kicl;

import java.util.List;
import java.util.function.Consumer;

import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.element.Channel;
import org.kitteh.irc.client.library.element.ServerMessage;
import org.kitteh.irc.client.library.element.User;
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;

public class TestChannelMessageEvent extends ChannelMessageEvent {

  private Consumer<String> sendReplyHandler;

  public TestChannelMessageEvent(final Client client,
      final List<ServerMessage> originalMessages,
      final User sender, final Channel channel,
      final String message) {
    super(client, originalMessages, sender, channel, message);
    this.sendReplyHandler = super::sendReply;
  }

  @Override
  public void sendReply(final String message) {
    this.sendReplyHandler.accept(message);
  }

  public final void setSendReplyHandler(final Consumer<String> handler) {
    this.sendReplyHandler = handler;
  }

}
