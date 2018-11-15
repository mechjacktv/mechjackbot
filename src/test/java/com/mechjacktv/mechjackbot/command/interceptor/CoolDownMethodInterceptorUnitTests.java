package com.mechjacktv.mechjackbot.command.interceptor;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;

import com.google.inject.Provider;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.CommandUtils;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.NoCoolDown;
import com.mechjacktv.test.ArbitraryDataGenerator;

public class CoolDownMethodInterceptorUnitTests {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();
  private final CommandMethodInterceptorUnitTestUtils methodInterceptorUtils = new CommandMethodInterceptorUnitTestUtils(
      this.arbitraryDataGenerator);

  @SuppressWarnings("unchecked")
  private CoolDownMethodInterceptor givenASubjectToTest(final CommandUtils commandUtils) {
    final Provider<CommandUtils> provider = mock(Provider.class);

    when(provider.get()).thenReturn(commandUtils);
    return new CoolDownMethodInterceptor(provider);
  }

  @Test
  public final void invoke_isCooledDown_invokesCommand() throws Throwable {
    final CommandUtils commandUtils = mock(CommandUtils.class);
    final CoolDownMethodInterceptor subjectUnderTest = this.givenASubjectToTest(commandUtils);
    when(commandUtils.isCooledDown(isA(Command.class), isA(MessageEvent.class))).thenReturn(true);
    final MethodInvocation methodInvocation = this.methodInterceptorUtils.givenAFakeMethodInvocation();

    subjectUnderTest.invoke(methodInvocation);

    verify(methodInvocation).proceed();
  }

  @Test
  public final void invoke_isNotCooledDown_commandIsNotInvoked() throws Throwable {
    final CommandUtils commandUtils = mock(CommandUtils.class);
    final CoolDownMethodInterceptor subjectUnderTest = this.givenASubjectToTest(commandUtils);
    when(commandUtils.isCooledDown(isA(Command.class), isA(MessageEvent.class))).thenReturn(false);
    final MethodInvocation methodInvocation = this.methodInterceptorUtils.givenAFakeMethodInvocation();

    subjectUnderTest.invoke(methodInvocation);

    verify(methodInvocation, never()).proceed();
  }

  @Test
  @NoCoolDown
  public final void invoke_noCoolDown_invokesCommand() throws Throwable {
    final CommandUtils commandUtils = mock(CommandUtils.class);
    final CoolDownMethodInterceptor subjectUnderTest = this.givenASubjectToTest(commandUtils);
    when(commandUtils.isCooledDown(isA(Command.class), isA(MessageEvent.class))).thenReturn(true);
    final Method method = this.getClass().getMethod("invoke_noCoolDown_doesNotCheckCoolDownStatus");
    final MethodInvocation methodInvocation = this.methodInterceptorUtils.givenAFakeMethodInvocation(method);

    subjectUnderTest.invoke(methodInvocation);

    verify(methodInvocation).proceed();
  }

  @Test
  @NoCoolDown
  public final void invoke_noCoolDown_doesNotCheckCoolDownStatus() throws Throwable {
    final CommandUtils commandUtils = mock(CommandUtils.class);
    final CoolDownMethodInterceptor subjectUnderTest = this.givenASubjectToTest(commandUtils);
    when(commandUtils.isCooledDown(isA(Command.class), isA(MessageEvent.class))).thenReturn(true);
    final Method method = this.getClass().getMethod("invoke_noCoolDown_doesNotCheckCoolDownStatus");
    final MethodInvocation methodInvocation = this.methodInterceptorUtils.givenAFakeMethodInvocation(method);

    subjectUnderTest.invoke(methodInvocation);

    verify(commandUtils, never()).isCooledDown(isA(Command.class), isA(MessageEvent.class));
  }

}
