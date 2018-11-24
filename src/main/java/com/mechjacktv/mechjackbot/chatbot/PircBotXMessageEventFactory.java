package com.mechjacktv.mechjackbot.chatbot;

import org.pircbotx.hooks.types.GenericMessageEvent;

import com.mechjacktv.mechjackbot.AppConfiguration;
import com.mechjacktv.mechjackbot.ChatBotConfiguration;
import com.mechjacktv.mechjackbot.CommandUtils;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.util.ExecutionUtils;

class PircBotXMessageEventFactory implements MessageEventFactory<GenericMessageEvent> {

  private final AppConfiguration appConfiguration;
  private final ChatBotConfiguration chatBotConfiguration;
  private final ChatBotFactory chatBotFactory;
  private final CommandUtils commandUtils;
  private final ExecutionUtils executionUtils;

  PircBotXMessageEventFactory(final AppConfiguration appConfiguration,
      final ChatBotConfiguration chatBotConfiguration, final ChatBotFactory chatBotFactory,
      final CommandUtils commandUtils, final ExecutionUtils executionUtils) {
    this.appConfiguration = appConfiguration;
    this.chatBotConfiguration = chatBotConfiguration;
    this.chatBotFactory = chatBotFactory;
    this.commandUtils = commandUtils;
    this.executionUtils = executionUtils;
  }

  @Override
  public final MessageEvent create(final GenericMessageEvent event) {
    return new PircBotXMessageEvent(this.appConfiguration, this.chatBotConfiguration, this.chatBotFactory,
        this.commandUtils, this.executionUtils, event);
  }
}
