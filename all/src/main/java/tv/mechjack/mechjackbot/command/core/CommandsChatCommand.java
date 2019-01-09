package tv.mechjack.mechjackbot.command.core;

import java.util.stream.Collectors;

import javax.inject.Inject;

import tv.mechjack.mechjackbot.ChatCommand;
import tv.mechjack.mechjackbot.ChatCommandRegistry;
import tv.mechjack.mechjackbot.ChatMessageEvent;
import tv.mechjack.mechjackbot.command.BaseChatCommand;
import tv.mechjack.mechjackbot.command.CommandConfigurationBuilder;

public final class CommandsChatCommand extends BaseChatCommand {

  public static final String DEFAULT_DESCRIPTION = "Lists all the commands available to users.";
  public static final String DEFAULT_MESSAGE_FORMAT = "Commands: %s";
  public static final String DEFAULT_TRIGGER = "!commands";

  private final ChatCommandRegistry chatCommandRegistry;

  @Inject
  CommandsChatCommand(final CommandConfigurationBuilder commandConfigurationBuilder,
      final ChatCommandRegistry chatCommandRegistry) {
    super(commandConfigurationBuilder.setTrigger(DEFAULT_TRIGGER)
        .setDescription(DEFAULT_DESCRIPTION)
        .setMessageFormat(DEFAULT_MESSAGE_FORMAT));
    this.chatCommandRegistry = chatCommandRegistry;
  }

  @Override
  public void handleMessageEvent(final ChatMessageEvent chatMessageEvent) {
    this.sendResponse(chatMessageEvent,
        this.chatCommandRegistry.getCommands().stream().filter(ChatCommand::isTriggerable)
            .map(command -> command.getTrigger().value).sorted().collect(Collectors.joining(" ")));
  }

}
