package com.mechjacktv.mechjackbot.command;

import javax.inject.Inject;

import com.mechjacktv.configuration.StringProperty;
import com.mechjacktv.mechjackbot.*;

public class TestCommand extends AbstractCommand {

  private static final StringProperty COMMAND_TRIGGER = new StringProperty(TestCommand.class, "trigger", "!test");

  @Inject
  protected TestCommand(final com.mechjacktv.configuration.Configuration configuration,
      final CommandUtils commandUtils) {
    super(new Configuration(configuration, commandUtils, CommandDescription.of("Testing. Testing."),
        CommandTriggerKey.of(COMMAND_TRIGGER.getKey()), CommandTrigger.of(COMMAND_TRIGGER.getDefaultValue())));
  }

  @Override
  @RestrictToAccessLevel(AccessLevel.OWNER)
  public void handleMessageEvent(final MessageEvent messageEvent) {
    messageEvent.sendResponse(Message.of(String.format("key: %s, default: %s, value: %s", COMMAND_TRIGGER.getKey(),
        COMMAND_TRIGGER.getDefaultValue(), COMMAND_TRIGGER.getValue())));
  }
}
