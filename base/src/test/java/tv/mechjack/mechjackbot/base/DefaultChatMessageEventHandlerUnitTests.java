package tv.mechjack.mechjackbot.base;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import com.google.common.collect.Sets;

import org.assertj.core.api.SoftAssertions;
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
import tv.mechjack.testframework.fake.FakeBuilder;
import tv.mechjack.testframework.fake.InvocationCounter;

public class DefaultChatMessageEventHandlerUnitTests extends ChatMessageEventHandlerContractTests {

  @Override
  protected DefaultChatMessageEventHandler givenASubjectToTest(final Set<ChatCommand> chatCommands) {
    return this.givenASubjectToTest(chatCommands, this.testFrameworkRule.fakeFactory().fake(Logger.class));
  }

  private DefaultChatMessageEventHandler givenASubjectToTest(final Set<ChatCommand> chatCommands,
      final Logger logger) {
    return new DefaultChatMessageEventHandler(chatCommands,
        this.testFrameworkRule.getInstance(ChatCommandRegistry.class),
        this.testFrameworkRule.getInstance(ChatCommandUtils.class), (name) -> logger);
  }

  @Test
  public final void handleMessageEvent_isCalled_logsAnDebugLevelMessage() {
    this.installModules();
    final FakeBuilder<Logger> fakeBuilder = this.testFrameworkRule.fakeBuilder(Logger.class);
    final InvocationCounter countingHandler = new InvocationCounter();
    fakeBuilder.forMethod("debug", new Class[] { String.class }).setHandler(countingHandler);
    final TestChatCommand command = this.testFrameworkRule.getInstance(TestChatCommand.class);
    command.setTriggered(true);
    final DefaultChatMessageEventHandler subjectUnderTest = this.givenASubjectToTest(Sets.newHashSet(command),
        fakeBuilder.build());

    subjectUnderTest.handleMessageEvent(this.testFrameworkRule.getInstance(ChatMessageEvent.class));

    assertThat(countingHandler.getCallCount()).isOne();
  }

  @Test
  public final void handleMessageEvent_commandThrowsException_logsAnDebugAndErrorLevelMessageWithSameLogger() {
    this.installModules();
    final FakeBuilder<Logger> fakeBuilder = this.testFrameworkRule.fakeBuilder(Logger.class);
    final InvocationCounter infoCountingHandler = new InvocationCounter();
    final InvocationCounter errorCountingHandler = new InvocationCounter();
    fakeBuilder.forMethod("debug", new Class[] { String.class }).setHandler(infoCountingHandler);
    fakeBuilder.forMethod("error", new Class[] { String.class, Throwable.class })
        .setHandler(errorCountingHandler);
    final TestChatCommand command = this.testFrameworkRule.getInstance(TestChatCommand.class);
    command.setTriggered(true);
    command.setMessageEventHandler(messageEvent -> {
      throw new StronglyTypedInstantiationException(new Exception());
    });
    final DefaultChatMessageEventHandler subjectUnderTest = this.givenASubjectToTest(Sets.newHashSet(command),
        fakeBuilder.build());

    subjectUnderTest.handleMessageEvent(this.testFrameworkRule.getInstance(ChatMessageEvent.class));

    final SoftAssertions softly = new SoftAssertions();
    softly.assertThat(infoCountingHandler.getCallCount()).isOne();
    softly.assertThat(errorCountingHandler.getCallCount()).isOne();
    softly.assertAll();
  }

  @Test
  public final void handleMessageEvent_commandWithDefaultAccessLevel_resultIsRequiredModeratorAccessLevel() {
    this.installModules();
    final DefaultAccessLevelChatCommand command = new DefaultAccessLevelChatCommand(
        this.testFrameworkRule.getInstance(CommandConfigurationBuilder.class));
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
