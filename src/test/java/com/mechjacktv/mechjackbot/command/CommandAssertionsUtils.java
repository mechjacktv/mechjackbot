package com.mechjacktv.mechjackbot.command;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.CommandUtils;
import com.mechjacktv.mechjackbot.Message;
import com.mechjacktv.mechjackbot.MessageEvent;

public class CommandAssertionsUtils {

  private final CommandUtils commandUtils;

  @Inject
  CommandAssertionsUtils(final CommandUtils commandUtils) {
    this.commandUtils = commandUtils;
  }

  public final void assertUsageMessageForCommand(final Message result, final Command subjectUnderTest,
      final MessageEvent messageEvent) {
    assertThat(result).isEqualTo(this.commandUtils.createUsageMessage(subjectUnderTest, messageEvent));
  }
}
