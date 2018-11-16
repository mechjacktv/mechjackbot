package com.mechjacktv.mechjackbot;

import com.mechjacktv.util.ArbitraryDataGenerator;

public class ArbitraryCommand extends AbstractCommand {

  private MessageEvent handledMessageEvent;

  public ArbitraryCommand(final AppConfiguration appConfiguration, final CommandUtils commandUtils,
      final ArbitraryDataGenerator arbitraryDataGenerator) {
    this(appConfiguration, commandUtils, arbitraryDataGenerator, true);
  }

  public ArbitraryCommand(final AppConfiguration appConfiguration, final CommandUtils commandUtils,
      final ArbitraryDataGenerator arbitraryDataGenerator, final boolean triggerable) {
    super(new Configuration(appConfiguration, commandUtils, CommandDescription.of(arbitraryDataGenerator.getString()),
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
