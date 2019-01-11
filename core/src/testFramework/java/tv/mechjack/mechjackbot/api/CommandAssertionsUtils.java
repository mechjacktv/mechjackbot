package tv.mechjack.mechjackbot.api;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

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
