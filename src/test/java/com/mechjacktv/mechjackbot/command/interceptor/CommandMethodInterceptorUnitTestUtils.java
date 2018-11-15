package com.mechjacktv.mechjackbot.command.interceptor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.aopalliance.intercept.MethodInvocation;

import com.mechjacktv.ArbitraryDataGenerator;
import com.mechjacktv.mechjackbot.ChatUser;
import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.CommandName;
import com.mechjacktv.mechjackbot.MessageEvent;

final class CommandMethodInterceptorUnitTestUtils {

  private final ArbitraryDataGenerator arbitraryDataGenerator;

  CommandMethodInterceptorUnitTestUtils(final ArbitraryDataGenerator arbitraryDataGenerator) {
    this.arbitraryDataGenerator = arbitraryDataGenerator;
  }

  MethodInvocation givenAFakeMethodInvocation() {
    final MethodInvocation methodInvocation = mock(MethodInvocation.class);
    final Command command = mock(Command.class);
    when(command.getName()).thenReturn(CommandName.of(this.arbitraryDataGenerator.getString()));
    final MessageEvent messageEvent = mock(MessageEvent.class);
    when(messageEvent.getChatUser()).thenReturn(mock(ChatUser.class));

    when(methodInvocation.getThis()).thenReturn(command);
    when(methodInvocation.getArguments()).thenReturn(new Object[] { messageEvent });
    return methodInvocation;
  }

}
