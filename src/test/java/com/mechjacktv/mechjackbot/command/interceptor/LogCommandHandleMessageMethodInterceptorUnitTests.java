package com.mechjacktv.mechjackbot.command.interceptor;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

import java.util.function.Function;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.slf4j.Logger;

import com.mechjacktv.util.ArbitraryDataGenerator;

public class LogCommandHandleMessageMethodInterceptorUnitTests {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();
  private final CommandMethodInterceptorUnitTestUtils methodInterceptorUtils = new CommandMethodInterceptorUnitTestUtils(
      this.arbitraryDataGenerator);

  private LogCommandHandleMessageMethodInterceptor givenIHaveASubjectToTest(
      final Function<String, Logger> loggerFactory) {
    return new LogCommandHandleMessageMethodInterceptor(loggerFactory);
  }

  @Test
  @SuppressWarnings("unchecked")
  public final void invoke_isCalled_logsAnInfoLevelMessage() throws NoSuchMethodException {
    final Function<String, Logger> loggerFactory = mock(Function.class);
    final Logger logger = mock(Logger.class);
    when(loggerFactory.apply(isA(String.class))).thenReturn(logger);
    final LogCommandHandleMessageMethodInterceptor subjectUnderTest = this.givenIHaveASubjectToTest(loggerFactory);

    subjectUnderTest.invoke(this.methodInterceptorUtils.givenAFakeMethodInvocation());

    verify(logger).info(isA(String.class));
  }

  @Test
  @SuppressWarnings("unchecked")
  public final void invoke_commandThrowsException_logsAnInfoAndErrorLevelMessageWithSameLogger() throws Throwable {
    final Function<String, Logger> loggerFactory = mock(Function.class);
    final Logger logger = mock(Logger.class);
    when(loggerFactory.apply(isA(String.class))).thenReturn(logger);
    final LogCommandHandleMessageMethodInterceptor subjectUnderTest = this.givenIHaveASubjectToTest(loggerFactory);
    final MethodInvocation methodInvocation = this.methodInterceptorUtils.givenAFakeMethodInvocation();
    when(methodInvocation.proceed()).thenThrow(RuntimeException.class);

    subjectUnderTest.invoke(methodInvocation);

    verify(logger).info(isA(String.class));
    verify(logger).error(isA(String.class), isA(Throwable.class));
    verify(loggerFactory).apply(isA(String.class));
  }

}
