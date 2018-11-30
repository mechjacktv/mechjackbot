package com.mechjacktv.mechjackbot;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.mechjackbot.command.BaseCommand;
import com.mechjacktv.mechjackbot.command.DefaultCommandConfigurationBuilder;
import com.mechjacktv.util.ArbitraryDataGenerator;
import com.mechjacktv.util.DefaultExecutionUtils;
import com.mechjacktv.util.ExecutionUtils;

public class ArbitraryCommand extends BaseCommand {

  public ArbitraryCommand(final Configuration configuration, final CommandUtils commandUtils,
      final ArbitraryDataGenerator arbitraryDataGenerator) {
    this(configuration, commandUtils, new DefaultExecutionUtils(), arbitraryDataGenerator, true);
  }

  public ArbitraryCommand(final Configuration configuration, final CommandUtils commandUtils,
      final ExecutionUtils executionUtils, final ArbitraryDataGenerator arbitraryDataGenerator,
      final boolean triggerable) {
    super(new DefaultCommandConfigurationBuilder(commandUtils, configuration, executionUtils)
        .setTriggerable(triggerable)
        .setDescription(arbitraryDataGenerator.getString())
        .setTrigger(arbitraryDataGenerator.getString()));
  }

  @Override
  public void handleMessageEvent(final MessageEvent messageEvent) {
    // empty
  }
}
