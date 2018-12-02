package com.mechjacktv.mechjackbot.command;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.testframework.ArbitraryDataGenerator;

public class ArbitraryNonTriggerableCommand extends BaseCommand {

  @Inject
  ArbitraryNonTriggerableCommand(final CommandConfigurationBuilder commandConfigurationBuilder,
      final ArbitraryDataGenerator arbitraryDataGenerator) {
    super(commandConfigurationBuilder.setDescription(arbitraryDataGenerator.getString()));
  }

  @Override
  public void handleMessageEvent(final MessageEvent messageEvent) {
    /* no-op (2018-12-02 mechjack) */
  }
}
