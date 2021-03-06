package tv.mechjack.mechjackbot.base;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tv.mechjack.mechjackbot.api.ChatCommand;
import tv.mechjack.mechjackbot.api.ChatCommandRegistry;
import tv.mechjack.mechjackbot.api.ChatCommandUtils;
import tv.mechjack.mechjackbot.api.ChatMessage;
import tv.mechjack.mechjackbot.api.ChatMessageEvent;
import tv.mechjack.mechjackbot.api.ChatMessageEventHandler;

public final class DefaultChatMessageEventHandler implements ChatMessageEventHandler {

  private final Function<String, Logger> loggerFactory;
  private final Map<String, Logger> loggers;
  private final ChatCommandRegistry chatCommandRegistry;
  private final ChatCommandUtils chatCommandUtils;

  @Inject
  DefaultChatMessageEventHandler(final Set<ChatCommand> chatCommands, final ChatCommandRegistry chatCommandRegistry,
      final ChatCommandUtils chatCommandUtils) {
    this(chatCommands, chatCommandRegistry, chatCommandUtils, LoggerFactory::getLogger);
  }

  DefaultChatMessageEventHandler(final Set<ChatCommand> chatCommands, final ChatCommandRegistry chatCommandRegistry,
      final ChatCommandUtils chatCommandUtils, final Function<String, Logger> loggerFactory) {
    this.chatCommandRegistry = chatCommandRegistry;
    this.chatCommandUtils = chatCommandUtils;
    for (final ChatCommand chatCommand : chatCommands) {
      this.chatCommandRegistry.addCommand(chatCommand);
    }
    this.loggerFactory = loggerFactory;
    this.loggers = new HashMap<>();
  }

  @Override
  public void handleMessageEvent(final ChatMessageEvent chatMessageEvent) {
    for (final ChatCommand chatCommand : this.chatCommandRegistry.getCommands()) {
      if (this.isTriggered(chatCommand, chatMessageEvent)) {
        this.getLogger(chatCommand.getName().value).debug(
            String.format("Executed: trigger=%s, user=%s, message=%s",
                chatCommand.getTrigger(),
                chatMessageEvent.getChatUser().getTwitchLogin(),
                chatMessageEvent.getChatMessage()));
        try {
          chatCommand.handleMessageEvent(chatMessageEvent);
        } catch (final Throwable t) {
          this.getLogger(chatCommand.getName().value).error(
              String.format("Failed: trigger=%s, user=%s, message=%s",
                  chatCommand.getTrigger(),
                  chatMessageEvent.getChatUser().getTwitchLogin(),
                  chatMessageEvent.getChatMessage()),
              t);
          // TODO (2018-12-23 mechjack): Decide if we want to report errors in chat like
          // this
          chatMessageEvent.sendResponse(ChatMessage.of(String.format("%s, %s failed: %s",
              chatMessageEvent.getChatUser().getTwitchLogin(), chatCommand.getTrigger(), t.getMessage())));
        }
      }
    }
  }

  private boolean isTriggered(final ChatCommand chatCommand, final ChatMessageEvent chatMessageEvent) {
    return chatCommand.isTriggered(chatMessageEvent)
        && this.chatCommandUtils.hasAccessLevel(chatCommand, chatMessageEvent)
        && this.chatCommandUtils.isCooledDown(chatCommand, chatMessageEvent);
  }

  private Logger getLogger(final String name) {
    if (this.loggers.containsKey(name)) {
      return this.loggers.get(name);
    }

    final Logger logger = this.loggerFactory.apply(name);

    this.loggers.put(name, logger);
    return logger;
  }

}
