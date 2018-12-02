package com.mechjacktv.mechjackbot.command;

import javax.inject.Inject;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.mechjackbot.CommandUtils;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.testframework.ArbitraryDataGenerator;
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

  @Inject
  ArbitraryCommand(final CommandConfigurationBuilder commandConfigurationBuilder,
      final ArbitraryDataGenerator arbitraryDataGenerator) {
    super(commandConfigurationBuilder.setDescription(arbitraryDataGenerator.getString())
        .setTrigger(arbitraryDataGenerator.getString()));
  }

  @Override
  public void handleMessageEvent(final MessageEvent messageEvent) {
    /* no-op (2018-12-02 mechjack) */
  }
}
