package com.mechjacktv.mechjackbot.command.interceptor;

import static com.mechjacktv.mechjackbot.command.interceptor.CommandHandleMessageMethodMatcher.MATCHING_METHOD_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Method;

import org.junit.Test;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.test.ArbitraryDataGenerator;

public class CommandHandleMessageMethodMatcherUnitTests {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();

  private Method givenAHandleMessageEventMethod() throws NoSuchMethodException {
    return mock(Command.class).getClass().getMethod(MATCHING_METHOD_NAME, MessageEvent.class);
  }

  private Method givenAnOtherMethod() throws NoSuchMethodException {
    return mock(Command.class).getClass().getMethod("isTriggered", MessageEvent.class);
  }

  @Test
  public final void matches_forHandleMessageEventMethod_returnsTrue() throws NoSuchMethodException {
    final Method method = this.givenAHandleMessageEventMethod();
    final CommandHandleMessageMethodMatcher subjectUnderTest = new CommandHandleMessageMethodMatcher();

    final boolean result = subjectUnderTest.matches(method);

    assertThat(result).isTrue();
  }

  @Test
  public final void matches_forOtherMethod_returnsFalse() throws NoSuchMethodException {
    final Method method = this.givenAnOtherMethod();
    final CommandHandleMessageMethodMatcher subjectUnderTest = new CommandHandleMessageMethodMatcher();

    final boolean result = subjectUnderTest.matches(method);

    assertThat(result).isFalse();
  }

}
