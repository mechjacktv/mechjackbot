package com.mechjacktv.mechjackbot;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.mechjackbot.command.AbstractCommand;
import com.mechjacktv.mechjackbot.command.DefaultCommandConfigurationBuilder;
import com.mechjacktv.util.ArbitraryDataGenerator;

public class ArbitraryCommand extends AbstractCommand {

  public ArbitraryCommand(final Configuration configuration, final CommandUtils commandUtils,
      final ArbitraryDataGenerator arbitraryDataGenerator) {
    this(configuration, commandUtils, arbitraryDataGenerator, true);
  }

  public ArbitraryCommand(final Configuration configuration,
      final CommandUtils commandUtils, final ArbitraryDataGenerator arbitraryDataGenerator, final boolean triggerable) {
    super(new DefaultCommandConfigurationBuilder(commandUtils, configuration)
        .setTriggerable(triggerable)
        .setDescription(arbitraryDataGenerator.getString())
        .setTrigger(arbitraryDataGenerator.getString()));
  }

  @Override
  public void handleMessageEvent(final MessageEvent messageEvent) {
    // empty
  }
}
