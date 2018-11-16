package com.mechjacktv.mechjackbot.command.interceptor;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;

import com.google.inject.Provider;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;

import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.util.ArbitraryDataGenerator;

public class RestrictToRolesMethodInterceptorUnitTests {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();
  private final CommandMethodInterceptorUnitTestUtils methodInterceptorUtils = new CommandMethodInterceptorUnitTestUtils(
      this.arbitraryDataGenerator);

  @SuppressWarnings("unchecked")
  private RestrictToRolesMethodInterceptor givenASubjectToTest(final CommandUtils commandUtils) {
    final Provider<CommandUtils> provider = mock(Provider.class);

    when(provider.get()).thenReturn(commandUtils);
    return new RestrictToRolesMethodInterceptor(provider);
  }

  @Test
  @RestrictToRoles({ ViewerRole.SUBSCRIBER })
  public final void invoke_hasRole_invokesCommand() throws Throwable {
    final CommandUtils commandUtils = mock(CommandUtils.class);
    final RestrictToRolesMethodInterceptor subjectUnderTest = this.givenASubjectToTest(commandUtils);
    final Method method = this.getClass().getMethod("invoke_hasRole_invokesCommand");
    when(commandUtils.hasRole(isA(Command.class), isA(MessageEvent.class))).thenReturn(true);
    final MethodInvocation methodInvocation = this.methodInterceptorUtils.givenAFakeMethodInvocation(method);

    subjectUnderTest.invoke(methodInvocation);

    verify(methodInvocation).proceed();
  }

  @Test
  @RestrictToRoles({ ViewerRole.SUBSCRIBER })
  public final void invoke_doesNotHaveRole_commandIsNotInvoked() throws Throwable {
    final CommandUtils commandUtils = mock(CommandUtils.class);
    final RestrictToRolesMethodInterceptor subjectUnderTest = this.givenASubjectToTest(commandUtils);
    final Method method = this.getClass().getMethod("invoke_hasRole_invokesCommand");
    when(commandUtils.hasRole(isA(Command.class), isA(MessageEvent.class))).thenReturn(false);
    final MethodInvocation methodInvocation = this.methodInterceptorUtils.givenAFakeMethodInvocation(method);

    subjectUnderTest.invoke(methodInvocation);

    verify(methodInvocation, never()).proceed();
  }

}