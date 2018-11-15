package com.mechjacktv.mechjackbot.command.interceptor;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

import com.google.inject.Provider;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;

import com.mechjacktv.mechjackbot.CommandUtils;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.test.ArbitraryDataGenerator;

public class RestrictToRegularMethodInterceptorUnitTests {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();
  private final CommandMethodInterceptorUnitTestUtils methodInterceptorUtils = new CommandMethodInterceptorUnitTestUtils(
      this.arbitraryDataGenerator);

  @SuppressWarnings("unchecked")
  private RestrictToRegularMethodInterceptor givenASubjectToTest(final CommandUtils commandUtils) {
    final Provider<CommandUtils> provider = mock(Provider.class);

    when(provider.get()).thenReturn(commandUtils);
    return new RestrictToRegularMethodInterceptor(provider);
  }

  @Test
  public final void invoke_isRegularViewer_invokesCommand() throws Throwable {
    final CommandUtils commandUtils = mock(CommandUtils.class);
    final RestrictToRegularMethodInterceptor subjectUnderTest = this.givenASubjectToTest(commandUtils);
    when(commandUtils.isRegular(isA(MessageEvent.class))).thenReturn(true);
    final MethodInvocation methodInvocation = this.methodInterceptorUtils.givenAFakeMethodInvocation();

    subjectUnderTest.invoke(methodInvocation);

    verify(methodInvocation).proceed();
  }

  @Test
  public final void invoke_isNotRegularViewer_commandIsNotInvoked() throws Throwable {
    final CommandUtils commandUtils = mock(CommandUtils.class);
    final RestrictToRegularMethodInterceptor subjectUnderTest = this.givenASubjectToTest(commandUtils);
    when(commandUtils.isRegular(isA(MessageEvent.class))).thenReturn(false);
    final MethodInvocation methodInvocation = this.methodInterceptorUtils.givenAFakeMethodInvocation();

    subjectUnderTest.invoke(methodInvocation);

    verify(methodInvocation, never()).proceed();
  }

}
