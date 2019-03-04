package tv.mechjack.mechjackbot.chatbot.kicl;

import javax.inject.Inject;
import javax.inject.Provider;

import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;

import tv.mechjack.mechjackbot.api.ChatBot;
import tv.mechjack.mechjackbot.api.ChatMessageEvent;
import tv.mechjack.platform.configuration.Configuration;
import tv.mechjack.platform.utils.ExecutionUtils;

public class KiclChatMessageEventFactory {

  private final Provider<ChatBot> chatBotProvider;
  private final KiclChatChannelFactory chatMessageEventChannelFactory;
  private final KiclChatUserFactory chatUserFactory;
  private final Configuration configuration;
  private final ExecutionUtils executionUtils;

  @Inject
  KiclChatMessageEventFactory(final Provider<ChatBot> chatBotProvider,
      final KiclChatChannelFactory chatMessageEventChannelFactory,
      final KiclChatUserFactory chatUserFactory,
      final Configuration configuration, final ExecutionUtils executionUtils) {
    this.chatBotProvider = chatBotProvider;
    this.chatMessageEventChannelFactory = chatMessageEventChannelFactory;
    this.chatUserFactory = chatUserFactory;
    this.configuration = configuration;
    this.executionUtils = executionUtils;
  }

  public ChatMessageEvent create(final ChannelMessageEvent event) {
    return new KiclChatMessageEvent(event, this.chatBotProvider.get(),
        this.chatMessageEventChannelFactory, this.chatUserFactory,
        this.configuration, this.executionUtils);
  }

}
