package com.mechjacktv.mechjackbot.command.interceptor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;

import com.mechjacktv.mechjackbot.ChatUser;
import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.CommandName;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.util.ArbitraryDataGenerator;

final class CommandMethodInterceptorUnitTestUtils {

  private final ArbitraryDataGenerator arbitraryDataGenerator;

  CommandMethodInterceptorUnitTestUtils(final ArbitraryDataGenerator arbitraryDataGenerator) {
    this.arbitraryDataGenerator = arbitraryDataGenerator;
  }

  MethodInvocation givenAFakeMethodInvocation() throws NoSuchMethodException {
    return this.givenAFakeMethodInvocation(Object.class.getMethod("hashCode"));
  }

  MethodInvocation givenAFakeMethodInvocation(final Method method) {
    final MethodInvocation methodInvocation = mock(MethodInvocation.class);
    final Command command = mock(Command.class);
    when(command.getName()).thenReturn(CommandName.of(this.arbitraryDataGenerator.getString()));
    final MessageEvent messageEvent = mock(MessageEvent.class);
    when(messageEvent.getChatUser()).thenReturn(mock(ChatUser.class));

    when(methodInvocation.getThis()).thenReturn(command);
    when(methodInvocation.getArguments()).thenReturn(new Object[] { messageEvent });
    when(methodInvocation.getMethod()).thenReturn(method);
    return methodInvocation;
  }

}