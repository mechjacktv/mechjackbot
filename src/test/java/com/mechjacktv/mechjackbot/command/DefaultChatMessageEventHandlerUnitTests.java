package com.mechjacktv.mechjackbot.command;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Set;

import com.google.common.collect.Sets;

import org.junit.Test;
import org.slf4j.Logger;

import com.mechjacktv.mechjackbot.ChatMessageEventHandlerContractTests;
import com.mechjacktv.mechjackbot.ChatCommand;
import com.mechjacktv.mechjackbot.ChatCommandRegistry;
import com.mechjacktv.mechjackbot.ChatCommandUtils;
import com.mechjacktv.mechjackbot.ChatMessageEvent;
import com.mechjacktv.mechjackbot.TestChatCommand;

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
      throw new RuntimeException();
    });
    final Logger logger = mock(Logger.class);
    final DefaultChatMessageEventHandler subjectUnderTest = this.givenASubjectToTest(Sets.newHashSet(command), logger);

    subjectUnderTest.handleMessageEvent(this.testFrameworkRule.getInstance(ChatMessageEvent.class));

    verify(logger).info(isA(String.class));
    verify(logger).error(isA(String.class), isA(Throwable.class));
  }

}
