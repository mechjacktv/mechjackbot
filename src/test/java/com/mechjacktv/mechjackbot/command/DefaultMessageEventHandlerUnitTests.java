package com.mechjacktv.mechjackbot.command;

import java.util.Set;
import java.util.function.Function;

import com.google.common.collect.Sets;
import com.mechjacktv.configuration.MapConfiguration;
import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.util.DefaultExecutionUtils;
import com.mechjacktv.util.DefaultTimeUtils;
import com.mechjacktv.util.ExecutionUtils;

import org.junit.Test;
import org.slf4j.Logger;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

public class DefaultMessageEventHandlerUnitTests extends MessageEventHandlerContractTests {

  @Override
  protected DefaultMessageEventHandler givenASubjectToTest(final Set<Command> commands) {
    return this.givenASubjectToTest(commands, mock(Logger.class));
  }

  protected DefaultMessageEventHandler givenASubjectToTest(final Set<Command> commands, final Logger logger) {
    final ExecutionUtils executionUtils = new DefaultExecutionUtils();
    final CommandRegistry commandRegistry = new DefaultCommandRegistry(new DefaultExecutionUtils());
    final CommandUtils commandUtils = new DefaultCommandUtils(new MapConfiguration(executionUtils), executionUtils,
        new DefaultTimeUtils());

    return new DefaultMessageEventHandler(commands, commandRegistry, commandUtils, this.givenALoggerFactory(logger));
  }

  @SuppressWarnings("unchecked")
  private Function<String, Logger> givenALoggerFactory(final Logger logger) {
    final Function<String, Logger> loggerFactory = mock(Function.class);

    when(loggerFactory.apply(isA(String.class))).thenReturn(logger);
    return loggerFactory;
  }

  @Test
  public final void handleMessageEvent_isCalled_logsAnInfoLevelMessage() {
    final Command command = this.givenAFakeCommand(true);
    final Logger logger = mock(Logger.class);
    final DefaultMessageEventHandler subjectUnderTest = this.givenASubjectToTest(Sets.newHashSet(command), logger);

    subjectUnderTest.handleMessageEvent(this.givenAFakeMessageEvent());

    verify(logger).info(isA(String.class));
  }

  @Test
  public final void handleMessageEvent_commandThrowsException_logsAnInfoAndErrorLevelMessageWithSameLogger()
      throws Throwable {
    final Command command = this.givenAFakeCommand(true);
    doThrow(RuntimeException.class).when(command).handleMessageEvent(isA(MessageEvent.class));
    final Logger logger = mock(Logger.class);
    final DefaultMessageEventHandler subjectUnderTest = this.givenASubjectToTest(Sets.newHashSet(command), logger);

    subjectUnderTest.handleMessageEvent(this.givenAFakeMessageEvent());

    verify(logger).info(isA(String.class));
    verify(logger).error(isA(String.class), isA(Throwable.class));
  }

}
