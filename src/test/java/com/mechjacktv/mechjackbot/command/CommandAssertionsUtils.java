package com.mechjacktv.mechjackbot.command;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.ChatCommand;
import com.mechjacktv.mechjackbot.ChatCommandUtils;
import com.mechjacktv.mechjackbot.ChatMessage;
import com.mechjacktv.mechjackbot.ChatMessageEvent;

public class CommandAssertionsUtils {

  private final ChatCommandUtils chatCommandUtils;

  @Inject
  CommandAssertionsUtils(final ChatCommandUtils chatCommandUtils) {
    this.chatCommandUtils = chatCommandUtils;
  }

  public final void assertUsageMessageForCommand(final ChatMessage result, final ChatCommand subjectUnderTest,
      final ChatMessageEvent chatMessageEvent) {
    final ChatMessage usageMessage = this.chatCommandUtils.createUsageMessage(subjectUnderTest, chatMessageEvent);

    assertThat(result).isEqualTo(this.chatCommandUtils.replaceChatMessageVariables(subjectUnderTest, chatMessageEvent,
        usageMessage));
  }

}
