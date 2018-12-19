package com.mechjacktv.mechjackbot.chatbot;

import javax.inject.Inject;

import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.types.GenericMessageEvent;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.mechjackbot.ChatBotConfiguration;
import com.mechjacktv.mechjackbot.CommandUtils;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.util.ExecutionUtils;

class PircBotXMessageEventFactory implements MessageEventFactory<GenericMessageEvent> {

  private final Configuration configuration;
  private final ChatBotConfiguration chatBotConfiguration;
  private final ChatBotFactory<PircBotX> chatBotFactory;
  private final ChatUserFactory<User> chatUserFactory;
  private final CommandUtils commandUtils;
  private final ExecutionUtils executionUtils;

  @Inject
  PircBotXMessageEventFactory(final Configuration configuration,
      final ChatBotConfiguration chatBotConfiguration, final ChatBotFactory<PircBotX> chatBotFactory,
      final ChatUserFactory<User> chatUserFactory,
      final CommandUtils commandUtils, final ExecutionUtils executionUtils) {
    this.configuration = configuration;
    this.chatBotConfiguration = chatBotConfiguration;
    this.chatBotFactory = chatBotFactory;
    this.chatUserFactory = chatUserFactory;
    this.commandUtils = commandUtils;
    this.executionUtils = executionUtils;
  }

  @Override
  public final MessageEvent create(final GenericMessageEvent event) {
    return new PircBotXMessageEvent(this.configuration, this.chatBotConfiguration, this.chatBotFactory,
        this.chatUserFactory, this.commandUtils, this.executionUtils, event);
  }

}
