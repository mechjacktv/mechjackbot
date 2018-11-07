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
      final CommandUtils commandUtils,
      final TwitchClient twitchClient) {
    super(appConfiguration, CommandTriggerKey.of(COMMAND_TRIGGER_KEY), CommandTrigger.of(COMMAND_TRIGGER_DEFAULT),
        commandUtils);
  }

  @Override
  public final CommandDescription getDescription() {
    return CommandDescription.of("A command I change freely to test development.");
  }

  @Override
  @RestrictToOwner
  @GlobalCoolDown
  public void handleMessage(final MessageEvent messageEvent) {
    messageEvent.sendResponse(Message.of("Your test is run"));
  }

  @Override
  public boolean isTriggerable() {
    return false;
  }

}
