package com.mechjacktv.mechjackbot.command.interceptor;

import com.google.inject.Provider;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;

import com.mechjacktv.ArbitraryDataGenerator;
import com.mechjacktv.mechjackbot.CommandUtils;
import com.mechjacktv.mechjackbot.MessageEvent;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

public class RestrictToOwnerMethodInterceptorUnitTests {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();
  private final CommandMethodInterceptorUnitTestUtils methodInterceptorUtils = new CommandMethodInterceptorUnitTestUtils(
      this.arbitraryDataGenerator);

  @SuppressWarnings("unchecked")
  private RestrictToOwnerMethodInterceptor givenASubjectToTest(final CommandUtils commandUtils) {
    final Provider<CommandUtils> provider = mock(Provider.class);

    when(provider.get()).thenReturn(commandUtils);
    return new RestrictToOwnerMethodInterceptor(provider);
  }

  @Test
  public final void invoke_isOwnerViewer_invokesCommand() throws Throwable {
    final CommandUtils commandUtils = mock(CommandUtils.class);
    final RestrictToOwnerMethodInterceptor subjectUnderTest = this.givenASubjectToTest(commandUtils);
    when(commandUtils.isChannelOwner(isA(MessageEvent.class))).thenReturn(true);
    final MethodInvocation methodInvocation = this.methodInterceptorUtils.givenAFakeMethodInvocation();

    subjectUnderTest.invoke(methodInvocation);

    verify(methodInvocation).proceed();
  }

  @Test
  public final void invoke_isNotOwnerViewer_commandIsNotInvoked() throws Throwable {
    final CommandUtils commandUtils = mock(CommandUtils.class);
    final RestrictToOwnerMethodInterceptor subjectUnderTest = this.givenASubjectToTest(commandUtils);
    when(commandUtils.isChannelOwner(isA(MessageEvent.class))).thenReturn(false);
    final MethodInvocation methodInvocation = this.methodInterceptorUtils.givenAFakeMethodInvocation();

    subjectUnderTest.invoke(methodInvocation);

    verify(methodInvocation, never()).proceed();
  }

}
