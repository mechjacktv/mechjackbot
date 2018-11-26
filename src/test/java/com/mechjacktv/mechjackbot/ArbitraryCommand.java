package com.mechjacktv.mechjackbot;

import com.mechjacktv.util.ArbitraryDataGenerator;

public class ArbitraryCommand extends AbstractCommand {

  private MessageEvent handledMessageEvent;

  public ArbitraryCommand(final com.mechjacktv.configuration.Configuration configuration,
      final CommandUtils commandUtils,
      final ArbitraryDataGenerator arbitraryDataGenerator) {
    this(configuration, commandUtils, arbitraryDataGenerator, true);
  }

  public ArbitraryCommand(final com.mechjacktv.configuration.Configuration configuration,
      final CommandUtils commandUtils,
      final ArbitraryDataGenerator arbitraryDataGenerator, final boolean triggerable) {
    super(new Configuration(configuration, commandUtils, CommandDescription.of(arbitraryDataGenerator.getString()),
        CommandTriggerKey.of(arbitraryDataGenerator.getString()), CommandTrigger.of(arbitraryDataGenerator.getString()))
            .setTriggerable(triggerable));
    this.handledMessageEvent = null;
  }

  public MessageEvent getHandledMessageEvent() {
    return this.handledMessageEvent;
  }

  @Override
  public void handleMessageEvent(final MessageEvent messageEvent) {
    this.handledMessageEvent = messageEvent;
  }
}
