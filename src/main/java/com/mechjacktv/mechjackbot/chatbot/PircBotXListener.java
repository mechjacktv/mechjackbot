package com.mechjacktv.mechjackbot.chatbot;

import java.util.Set;

import javax.inject.Inject;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.PingEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

import com.mechjacktv.mechjackbot.AppConfiguration;
import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.MessageEventHandler;
import com.mechjacktv.util.ExecutionUtils;

final class PircBotXListener extends ListenerAdapter {

  private final AppConfiguration appConfiguration;
  private final ExecutionUtils executionUtils;
  private final MessageEventHandler messageEventHandler;

  @Inject
  public PircBotXListener(final Set<Command> commands, final AppConfiguration appConfiguration,
      final ExecutionUtils executionUtils, final MessageEventHandler messageEventHandler) {
    this.appConfiguration = appConfiguration;
    this.executionUtils = executionUtils;
    this.messageEventHandler = messageEventHandler;
    for (final Command command : commands) {
      this.messageEventHandler.addCommand(command);
    }
  }

  @Override
  public final void onPing(final PingEvent event) {
    event.respond(String.format("PONG %s", event.getPingValue()));
  }

  @Override
  public final void onGenericMessage(final GenericMessageEvent event) {
    this.messageEventHandler
        .handleMessageEvent(new PircBotXMessageEvent(this.appConfiguration, this.executionUtils, event));
  }

}
