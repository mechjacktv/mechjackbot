package com.mechjacktv.mechjackbot.command;

import java.util.Set;

import com.google.common.collect.Sets;
import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.CommandRegistry;
import com.mechjacktv.mechjackbot.CommandUtils;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.MessageEventHandlerContractTests;
import com.mechjacktv.mechjackbot.TestCommand;

import org.junit.Test;
import org.slf4j.Logger;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DefaultMessageEventHandlerUnitTests extends MessageEventHandlerContractTests {

  @Override
  protected DefaultMessageEventHandler givenASubjectToTest(final Set<Command> commands) {
    return this.givenASubjectToTest(commands, mock(Logger.class));
  }

  protected DefaultMessageEventHandler givenASubjectToTest(final Set<Command> commands, final Logger logger) {
    return new DefaultMessageEventHandler(commands, this.testFrameworkRule.getInstance(CommandRegistry.class),
        this.testFrameworkRule.getInstance(CommandUtils.class), (name) -> logger);
  }

  @Test
  public final void handleMessageEvent_isCalled_logsAnInfoLevelMessage() {
    this.installModules();
    final TestCommand command = this.testFrameworkRule.getInstance(TestCommand.class);
    command.setTriggered(true);
    final Logger logger = mock(Logger.class);
    final DefaultMessageEventHandler subjectUnderTest = this.givenASubjectToTest(Sets.newHashSet(command), logger);

    subjectUnderTest.handleMessageEvent(this.testFrameworkRule.getInstance(MessageEvent.class));

    verify(logger).info(isA(String.class));
  }

  @Test
  public final void handleMessageEvent_commandThrowsException_logsAnInfoAndErrorLevelMessageWithSameLogger() {
    this.installModules();
    final TestCommand command = this.testFrameworkRule.getInstance(TestCommand.class);
    command.setTriggered(true);
    command.setMessageEventHandler(messageEvent -> {
      throw new RuntimeException();
    });
    final Logger logger = mock(Logger.class);
    final DefaultMessageEventHandler subjectUnderTest = this.givenASubjectToTest(Sets.newHashSet(command), logger);

    subjectUnderTest.handleMessageEvent(this.testFrameworkRule.getInstance(MessageEvent.class));

    verify(logger).info(isA(String.class));
    verify(logger).error(isA(String.class), isA(Throwable.class));
  }

}
