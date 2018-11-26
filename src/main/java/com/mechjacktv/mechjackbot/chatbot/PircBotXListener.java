package com.mechjacktv.mechjackbot.chatbot;

import java.util.Set;

import javax.inject.Inject;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.PingEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.mechjackbot.*;

final class PircBotXListener extends ListenerAdapter {

  static final String JOIN_EVENT_MESSAGE_KEY = "chat_bot.join_event.message";
  static final String JOIN_EVENT_MESSAGE_DEFAULT = "Present and ready for action";

  private final Configuration configuration;
  private final CommandRegistry commandRegistry;
  private final ChatBotFactory<PircBotX> chatBotFactory;
  private final MessageEventFactory<GenericMessageEvent> messageEventFactory;

  @Inject
  public PircBotXListener(final Set<Command> commands, final Configuration configuration,
      final CommandRegistry commandRegistry, final ChatBotFactory<PircBotX> chatBotFactory,
      final MessageEventFactory<GenericMessageEvent> messageEventFactory) {
    this.configuration = configuration;
    this.commandRegistry = commandRegistry;
    this.chatBotFactory = chatBotFactory;
    this.messageEventFactory = messageEventFactory;
    for (final Command command : commands) {
      this.commandRegistry.addCommand(command);
    }
  }

  @Override
  public final void onPing(final PingEvent event) {
    event.respond(String.format("PONG %s", event.getPingValue()));
  }

  @Override
  public final void onGenericMessage(final GenericMessageEvent event) {
    final MessageEvent messageEvent = this.messageEventFactory.create(event);

    for (final Command command : this.commandRegistry.getCommands()) {
      if (command.isTriggered(messageEvent)) {
        command.handleMessageEvent(messageEvent);
      }
    }
  }

  @Override
  public void onJoin(final JoinEvent event) {
    this.chatBotFactory.create(event.getBot()).sendMessage(TwitchChannel.of(event.getChannel().getName()),
        Message.of(this.configuration.get(JOIN_EVENT_MESSAGE_KEY, JOIN_EVENT_MESSAGE_DEFAULT)));
  }
}
