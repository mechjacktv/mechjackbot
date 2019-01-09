package tv.mechjack.mechjackbot.command;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import tv.mechjack.mechjackbot.ChatCommand;
import tv.mechjack.mechjackbot.ChatCommandUtils;
import tv.mechjack.mechjackbot.ChatMessage;
import tv.mechjack.mechjackbot.ChatMessageEvent;

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
