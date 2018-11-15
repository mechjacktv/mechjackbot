package com.mechjacktv.mechjackbot.chatbot;

import java.util.Set;

import javax.inject.Inject;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.PingEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

import com.mechjacktv.mechjackbot.AppConfiguration;
import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.CommandRegistry;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.util.ExecutionUtils;

final class PircBotXListener extends ListenerAdapter {

  public static final String JOIN_EVENT_MESSAGE_KEY = "chat_bot.join_event.message";
  public static final String JOIN_EVENT_MESSAGE_DEFAULT = "Present and ready for action";

  private final AppConfiguration appConfiguration;
  private final ExecutionUtils executionUtils;
  private final CommandRegistry commandRegistry;

  @Inject
  public PircBotXListener(final Set<Command> commands, final AppConfiguration appConfiguration,
      final ExecutionUtils executionUtils, final CommandRegistry commandRegistry) {
    this.appConfiguration = appConfiguration;
    this.executionUtils = executionUtils;
    this.commandRegistry = commandRegistry;
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
    final MessageEvent messageEvent = new PircBotXMessageEvent(this.appConfiguration,
        this.executionUtils, event);

    for (final Command command : this.commandRegistry.getCommands()) {
      if (command.isTriggered(messageEvent)) {
        command.handleMessageEvent(messageEvent);
      }
    }
  }

  @Override
  public void onJoin(final JoinEvent event) {
    final PircBotXChatBot chatBot = new PircBotXChatBot(this.appConfiguration, this.executionUtils, event.getBot());

    chatBot.sendMessage(event.getChannel().getName(), this.appConfiguration.get(JOIN_EVENT_MESSAGE_KEY,
        JOIN_EVENT_MESSAGE_DEFAULT));
  }
}
