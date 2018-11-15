package com.mechjacktv.mechjackbot.command.interceptor;

import java.util.function.Function;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mechjacktv.ArbitraryDataGenerator;
import com.mechjacktv.mechjackbot.ChatUser;
import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.CommandName;
import com.mechjacktv.mechjackbot.MessageEvent;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

public class LogCommandHandleMessageMethodInterceptorUnitTests {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();

  private LogCommandHandleMessageMethodInterceptor givenIHaveASubjectToTest() {
    return this.givenIHaveASubjectToTest(LoggerFactory::getLogger);
  }

  private LogCommandHandleMessageMethodInterceptor givenIHaveASubjectToTest(
      final Function<String, Logger> loggerFactory) {
    return new LogCommandHandleMessageMethodInterceptor(loggerFactory);
  }

  private MethodInvocation givenAFakeMethodInvocation() {
    final MethodInvocation methodInvocation = mock(MethodInvocation.class);
    final Command command = mock(Command.class);
    when(command.getName()).thenReturn(CommandName.of(this.arbitraryDataGenerator.getString()));
    final MessageEvent messageEvent = mock(MessageEvent.class);
    when(messageEvent.getChatUser()).thenReturn(mock(ChatUser.class));

    when(methodInvocation.getThis()).thenReturn(command);
    when(methodInvocation.getArguments()).thenReturn(new Object[] { messageEvent });
    return methodInvocation;
  }

  @Test
  @SuppressWarnings("unchecked")
  public final void invoke_isCalled_logsAnInfoLevelMessage() {
    final Function<String, Logger> loggerFactory = mock(Function.class);
    final Logger logger = mock(Logger.class);
    when(loggerFactory.apply(isA(String.class))).thenReturn(logger);
    final LogCommandHandleMessageMethodInterceptor subjectUnderTest = this.givenIHaveASubjectToTest(loggerFactory);
    final MethodInvocation methodInvocation = this.givenAFakeMethodInvocation();

    subjectUnderTest.invoke(methodInvocation);

    verify(logger).info(isA(String.class));
  }

  @Test
  @SuppressWarnings("unchecked")
  public final void invoke_commandThrowsException_logsAnInfoAndErrorLevelMessageWithSameLogger() throws Throwable {
    final Function<String, Logger> loggerFactory = mock(Function.class);
    final Logger logger = mock(Logger.class);
    when(loggerFactory.apply(isA(String.class))).thenReturn(logger);
    final LogCommandHandleMessageMethodInterceptor subjectUnderTest = this.givenIHaveASubjectToTest(loggerFactory);
    final MethodInvocation methodInvocation = this.givenAFakeMethodInvocation();
    when(methodInvocation.proceed()).thenThrow(RuntimeException.class);

    subjectUnderTest.invoke(methodInvocation);

    verify(logger).info(isA(String.class));
    verify(logger).error(isA(String.class), isA(Throwable.class));
    verify(loggerFactory).apply(isA(String.class));
  }

}
