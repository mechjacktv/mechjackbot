package com.mechjacktv.mechjackbot.command.interceptor;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

import com.google.inject.Provider;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.CommandUtils;
import com.mechjacktv.test.ArbitraryDataGenerator;

public class GlobalCoolDownMethodInterceptorUnitTests {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();
  private final CommandMethodInterceptorUnitTestUtils methodInterceptorUtils = new CommandMethodInterceptorUnitTestUtils(
      this.arbitraryDataGenerator);

  @SuppressWarnings("unchecked")
  private GlobalCoolDownMethodInterceptor givenASubjectToTest(final CommandUtils commandUtils) {
    final Provider<CommandUtils> provider = mock(Provider.class);

    when(provider.get()).thenReturn(commandUtils);
    return new GlobalCoolDownMethodInterceptor(provider);
  }

  @Test
  public final void invoke_isCooledDown_invokesCommand() throws Throwable {
    final CommandUtils commandUtils = mock(CommandUtils.class);
    final GlobalCoolDownMethodInterceptor subjectUnderTest = this.givenASubjectToTest(commandUtils);
    when(commandUtils.isGloballyCooledDown(isA(Command.class))).thenReturn(true);
    final MethodInvocation methodInvocation = this.methodInterceptorUtils.givenAFakeMethodInvocation();

    subjectUnderTest.invoke(methodInvocation);

    verify(methodInvocation).proceed();
  }

  @Test
  public final void invoke_isNotCooledDown_commandIsNotInvoked() throws Throwable {
    final CommandUtils commandUtils = mock(CommandUtils.class);
    final GlobalCoolDownMethodInterceptor subjectUnderTest = this.givenASubjectToTest(commandUtils);
    when(commandUtils.isGloballyCooledDown(isA(Command.class))).thenReturn(false);
    final MethodInvocation methodInvocation = this.methodInterceptorUtils.givenAFakeMethodInvocation();

    subjectUnderTest.invoke(methodInvocation);

    verify(methodInvocation, never()).proceed();
  }

}
