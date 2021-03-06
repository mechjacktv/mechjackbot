package tv.mechjack.mechjackbot.api;

import static org.assertj.core.api.Assertions.assertThat;
import static tv.mechjack.testframework.ArbitraryData.ARBITRARY_COLLECTION_SIZE;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.collect.Sets;

import org.junit.Rule;
import org.junit.Test;

import tv.mechjack.platform.configuration.TestConfigurationModule;
import tv.mechjack.platform.utils.TestUtilsModule;
import tv.mechjack.testframework.TestFramework;

public abstract class ChatMessageEventHandlerContractTests {

  @Rule
  public final TestFramework testFrameworkRule = new TestFramework();

  protected void installModules() {
    this.testFrameworkRule.registerModule(new TestCommandModule());
    this.testFrameworkRule.registerModule(new TestConfigurationModule());
    this.testFrameworkRule.registerModule(new TestUtilsModule());
  }

  protected abstract ChatMessageEventHandler givenASubjectToTest(
      final Set<ChatCommand> chatCommands);

  private Set<TestChatCommand> givenASetOfCommands() {
    final Set<TestChatCommand> commands = new HashSet<>();

    for (int i = 0; i < ARBITRARY_COLLECTION_SIZE; i++) {
      final TestCommandConfigurationBuilder builder = new TestCommandConfigurationBuilder(
          this.testFrameworkRule
              .getInstance(CommandConfigurationBuilder.class));
      builder.setDefaultTrigger(UUID.randomUUID().toString());
      final TestChatCommand command = new NoCoolDownChatCommand(builder);

      commands.add(command);
    }
    return commands;
  }

  @Test
  public final void handleMessageEvent_noCommandTriggered_resultIsNoCommandHandleMessageEventCalled() {
    this.installModules();
    final AtomicBoolean wasCalled = new AtomicBoolean(false);
    final TestChatCommand command = this.testFrameworkRule
        .getInstance(TestChatCommand.class);
    command.setMessageEventHandler(messageEvent -> wasCalled.set(true));
    final ChatMessageEventHandler subjectUnderTest = this
        .givenASubjectToTest(Sets.newHashSet(command));

    subjectUnderTest.handleMessageEvent(
        this.testFrameworkRule.getInstance(ChatMessageEvent.class));

    assertThat(wasCalled).isFalse();
  }

  @Test
  public final void handleMessageEvent_commandTriggered_resultIsTriggeredCommandHandleMessageEventCalled() {
    this.installModules();
    final AtomicBoolean wasCalled = new AtomicBoolean(false);
    final TestChatCommand command = this.testFrameworkRule
        .getInstance(TestChatCommand.class);
    command.setMessageEventHandler(messageEvent -> wasCalled.set(true));
    final TestChatMessageEvent messageEvent = this.testFrameworkRule
        .getInstance(TestChatMessageEvent.class);
    messageEvent.setChatMessage(ChatMessage.of(command.getTrigger().value));
    final ChatMessageEventHandler subjectUnderTest = this
        .givenASubjectToTest(Sets.newHashSet(command));

    subjectUnderTest.handleMessageEvent(messageEvent);

    assertThat(wasCalled).isTrue();
  }

  @Test
  public final void handleMessageEvent_multipleCommandsTriggered_resultIsAllTriggeredCommandHandleMessageEventCalled() {
    this.installModules();
    final AtomicInteger calls = new AtomicInteger(0);
    final Set<TestChatCommand> commands = this.givenASetOfCommands();
    for (final TestChatCommand command : commands) {
      command.setTriggered(true);
      command.setMessageEventHandler(messageEvent -> calls.getAndIncrement());
    }
    final ChatMessageEventHandler subjectUnderTest = this
        .givenASubjectToTest(new HashSet<>(commands));

    subjectUnderTest.handleMessageEvent(
        this.testFrameworkRule.getInstance(ChatMessageEvent.class));

    assertThat(calls.get()).isEqualTo(commands.size());
  }

  private static final class NoCoolDownChatCommand extends TestChatCommand {

    private NoCoolDownChatCommand(
        final TestCommandConfigurationBuilder commandConfigurationBuilder) {
      super(commandConfigurationBuilder);
    }

    @Override
    @NoCoolDown
    @RequiresAccessLevel(UserRole.VIEWER)
    public void handleMessageEvent(ChatMessageEvent chatMessageEvent) {
      super.handleMessageEvent(chatMessageEvent);
    }

  }

}
