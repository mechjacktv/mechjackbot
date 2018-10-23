package com.mechjacktv.mechjackbot.chatbot;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.MessageEventHandler;
import com.mechjacktv.util.ExecutionUtils;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.PingEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

import javax.inject.Inject;
import java.util.Set;

final class PircBotXListener extends ListenerAdapter {

  private final ExecutionUtils executionUtils;
  private final MessageEventHandler messageEventHandler;

  @Inject
  public PircBotXListener(final Set<Command> commands, final ExecutionUtils executionUtils,
                          final MessageEventHandler messageEventHandler) {
    for (final Command command : commands) {
      messageEventHandler.addCommand(command);
    }
    this.executionUtils = executionUtils;
    this.messageEventHandler = messageEventHandler;
  }

  @Override
  public final void onPing(final PingEvent event) {
    event.respond(String.format("PONG %s", event.getPingValue()));
  }

  @Override
  public final void onGenericMessage(final GenericMessageEvent event) {
    this.messageEventHandler.handleMessage(new PircBotXMessageEvent(executionUtils, event));
  }

}
