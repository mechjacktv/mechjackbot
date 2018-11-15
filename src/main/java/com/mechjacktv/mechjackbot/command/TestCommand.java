package com.mechjacktv.mechjackbot.command;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.twitchclient.TwitchClient;

@SuppressWarnings("CanBeFinal")
public class TestCommand extends AbstractCommand {

  private static final String COMMAND_TRIGGER_KEY = "command.test.trigger";
  private static final String COMMAND_TRIGGER_DEFAULT = "!test";

  @Inject
  public TestCommand(final AppConfiguration appConfiguration, final ChatBotConfiguration chatBotConfiguration,
      final CommandUtils commandUtils, final TwitchClient twitchClient) {
    super(appConfiguration, CommandDescription.of("A command I change freely to test development."),
        CommandTriggerKey.of(COMMAND_TRIGGER_KEY), CommandTrigger.of(COMMAND_TRIGGER_DEFAULT),
        commandUtils, false);
  }

  @Override
  @RestrictToOwner
  @GlobalCoolDown
  public void handleMessageEvent(final MessageEvent messageEvent) {
    messageEvent.sendResponse(Message.of("Your test is run"));
  }

}
