package tv.mechjack.mechjackbot.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Set;

import com.google.common.collect.Sets;

import org.junit.Test;
import org.slf4j.Logger;

import tv.mechjack.mechjackbot.api.ChatCommand;
import tv.mechjack.mechjackbot.api.ChatCommandRegistry;
import tv.mechjack.mechjackbot.api.ChatCommandUtils;
import tv.mechjack.mechjackbot.api.ChatMessageEvent;
import tv.mechjack.mechjackbot.api.ChatMessageEventHandlerContractTests;
import tv.mechjack.mechjackbot.api.CommandConfigurationBuilder;
import tv.mechjack.mechjackbot.api.TestChatCommand;
import tv.mechjack.mechjackbot.api.TestChatMessageEvent;
import tv.mechjack.mechjackbot.api.TestChatUser;
import tv.mechjack.mechjackbot.api.UserRole;
import tv.mechjack.platform.utils.typedobject.StronglyTypedInstantiationException;

public class DefaultChatMessageEventHandlerUnitTests extends ChatMessageEventHandlerContractTests {

  @Override
  protected DefaultChatMessageEventHandler givenASubjectToTest(final Set<ChatCommand> chatCommands) {
    return this.givenASubjectToTest(chatCommands, mock(Logger.class));
  }

  protected DefaultChatMessageEventHandler givenASubjectToTest(final Set<ChatCommand> chatCommands,
      final Logger logger) {
    return new DefaultChatMessageEventHandler(chatCommands,
        this.testFrameworkRule.getInstance(ChatCommandRegistry.class),
        this.testFrameworkRule.getInstance(ChatCommandUtils.class), (name) -> logger);
  }

  @Test
  public final void handleMessageEvent_isCalled_logsAnInfoLevelMessage() {
    this.installModules();
    final TestChatCommand command = this.testFrameworkRule.getInstance(TestChatCommand.class);
    command.setTriggered(true);
    final Logger logger = mock(Logger.class);
    final DefaultChatMessageEventHandler subjectUnderTest = this.givenASubjectToTest(Sets.newHashSet(command), logger);

    subjectUnderTest.handleMessageEvent(this.testFrameworkRule.getInstance(ChatMessageEvent.class));

    verify(logger).info(isA(String.class));
  }

  @Test
  public final void handleMessageEvent_commandThrowsException_logsAnInfoAndErrorLevelMessageWithSameLogger() {
    this.installModules();
    final TestChatCommand command = this.testFrameworkRule.getInstance(TestChatCommand.class);
    command.setTriggered(true);
    command.setMessageEventHandler(messageEvent -> {
      throw new StronglyTypedInstantiationException(new Exception());
    });
    final Logger logger = mock(Logger.class);
    final DefaultChatMessageEventHandler subjectUnderTest = this.givenASubjectToTest(Sets.newHashSet(command), logger);

    subjectUnderTest.handleMessageEvent(this.testFrameworkRule.getInstance(ChatMessageEvent.class));

    verify(logger).info(isA(String.class));
    verify(logger).error(isA(String.class), isA(Throwable.class));
  }

  @Test
  public final void handleMessageEvent_commandWithDefaultAccessLevel_resultIsRequiredModeratorAccessLevel() {
    this.installModules();
    final DefaultAccessLevelChatCommand command =
        new DefaultAccessLevelChatCommand(this.testFrameworkRule.getInstance(CommandConfigurationBuilder.class));
    command.setTriggered(true);
    final TestChatUser user = this.testFrameworkRule.getInstance(TestChatUser.class);
    final TestChatMessageEvent event = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    event.setChatUser(user);
    final DefaultChatMessageEventHandler subjectUnderTest = this.givenASubjectToTest(Sets.newHashSet(command));

    final UserRole[] result = new UserRole[1];
    user.setHasAccessLevelHandler(userRole -> {
      result[0] = userRole;
      return true;
    });
    subjectUnderTest.handleMessageEvent(event);

    assertThat(result[0]).isEqualTo(UserRole.MODERATOR);
  }

  private static final class DefaultAccessLevelChatCommand extends TestChatCommand {

    private DefaultAccessLevelChatCommand(final CommandConfigurationBuilder commandConfigurationBuilder) {
      super(commandConfigurationBuilder);
    }

    @Override
    public void handleMessageEvent(ChatMessageEvent chatMessageEvent) {
      super.handleMessageEvent(chatMessageEvent);
    }

  }

}
