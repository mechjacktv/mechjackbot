package com.mechjacktv.mechjackbot;

import static com.mechjacktv.testframework.TestFrameworkRule.ARBITRARY_COLLECTION_SIZE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.collect.Sets;

import org.junit.Rule;
import org.junit.Test;

import com.mechjacktv.configuration.ConfigurationTestModule;
import com.mechjacktv.mechjackbot.command.CommandConfigurationBuilder;
import com.mechjacktv.mechjackbot.command.CommandTestModule;
import com.mechjacktv.testframework.TestFrameworkRule;
import com.mechjacktv.util.UtilTestModule;

public abstract class MessageEventHandlerContractTests {

  @Rule
  public final TestFrameworkRule testFrameworkRule = new TestFrameworkRule();

  protected void installModules() {
    this.testFrameworkRule.installModule(new CommandTestModule());
    this.testFrameworkRule.installModule(new ConfigurationTestModule());
    this.testFrameworkRule.installModule(new UtilTestModule());
  }

  protected abstract MessageEventHandler givenASubjectToTest(final Set<Command> commands);

  private Set<TestCommand> givenASetOfCommands() {
    final Set<TestCommand> commands = new HashSet<>();

    for (int i = 0; i < ARBITRARY_COLLECTION_SIZE; i++) {
      final TestCommandConfigurationBuilder builder = new TestCommandConfigurationBuilder(
          this.testFrameworkRule.getInstance(CommandConfigurationBuilder.class));
      builder.setDefaultTrigger(UUID.randomUUID().toString());
      final TestCommand command = new NoCoolDownCommand(builder);

      commands.add(command);
    }
    return commands;
  }

  @Test
  public final void handleMessageEvent_noCommandTriggered_resultIsNoCommandHandleMessageEventCalled() {
    this.installModules();
    final AtomicBoolean wasCalled = new AtomicBoolean(false);
    final TestCommand command = this.testFrameworkRule.getInstance(TestCommand.class);
    command.setMessageEventHandler(messageEvent -> wasCalled.set(true));
    final MessageEventHandler subjectUnderTest = this.givenASubjectToTest(Sets.newHashSet(command));

    subjectUnderTest.handleMessageEvent(this.testFrameworkRule.getInstance(MessageEvent.class));

    assertThat(wasCalled).isFalse();
  }

  @Test
  public final void handleMessageEvent_commandTriggered_resultIsTriggeredCommandHandleMessageEventCalled() {
    this.installModules();
    final AtomicBoolean wasCalled = new AtomicBoolean(false);
    final TestCommand command = this.testFrameworkRule.getInstance(TestCommand.class);
    command.setMessageEventHandler(messageEvent -> wasCalled.set(true));
    final TestMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestMessageEvent.class);
    messageEvent.setMessage(Message.of(command.getTrigger().value));
    final MessageEventHandler subjectUnderTest = this.givenASubjectToTest(Sets.newHashSet(command));

    subjectUnderTest.handleMessageEvent(messageEvent);

    assertThat(wasCalled).isTrue();
  }

  @Test
  public final void handleMessageEvent_multipleCommandsTriggered_resultIsAllTriggeredCommandHandleMessageEventCalled() {
    this.installModules();
    final AtomicInteger calls = new AtomicInteger(0);
    final Set<TestCommand> commands = this.givenASetOfCommands();
    for (final TestCommand command : commands) {
      command.setTriggered(true);
      command.setMessageEventHandler(messageEvent -> calls.getAndIncrement());
    }
    final MessageEventHandler subjectUnderTest = this.givenASubjectToTest(new HashSet<>(commands));

    subjectUnderTest.handleMessageEvent(this.testFrameworkRule.getInstance(MessageEvent.class));

    assertThat(calls.get()).isEqualTo(commands.size());
  }

  private static final class NoCoolDownCommand extends TestCommand {

    private NoCoolDownCommand(final TestCommandConfigurationBuilder commandConfigurationBuilder) {
      super(commandConfigurationBuilder);
    }

    @Override
    @NoCoolDown
    public void handleMessageEvent(MessageEvent messageEvent) {
      super.handleMessageEvent(messageEvent);
    }

  }

}
