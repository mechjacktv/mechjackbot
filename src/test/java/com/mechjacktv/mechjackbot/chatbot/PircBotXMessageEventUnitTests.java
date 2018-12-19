package com.mechjacktv.mechjackbot.chatbot;

import static com.mechjacktv.mechjackbot.chatbot.PircBotXMessageEvent.RESPONSE_MESSAGE_FORMAT_DEFAULT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.inject.Key;
import com.google.inject.TypeLiteral;

import org.assertj.core.api.SoftAssertions;
import org.junit.Rule;
import org.junit.Test;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.types.GenericMessageEvent;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.configuration.ConfigurationTestModule;
import com.mechjacktv.mechjackbot.ChatBot;
import com.mechjacktv.mechjackbot.ChatBotConfiguration;
import com.mechjacktv.mechjackbot.ChatUser;
import com.mechjacktv.mechjackbot.CommandUtils;
import com.mechjacktv.mechjackbot.Message;
import com.mechjacktv.mechjackbot.command.CommandTestModule;
import com.mechjacktv.testframework.TestFrameworkRule;
import com.mechjacktv.util.ExecutionUtils;
import com.mechjacktv.util.UtilTestModule;

public class PircBotXMessageEventUnitTests {

  @Rule
  public final TestFrameworkRule testFrameworkRule = new TestFrameworkRule();

  private void installModules() {
    this.testFrameworkRule.installModule(new ChatBotTestModule());
    this.testFrameworkRule.installModule(new CommandTestModule());
    this.testFrameworkRule.installModule(new ConfigurationTestModule());
    this.testFrameworkRule.installModule(new UtilTestModule());
  }

  private PircBotXMessageEvent givenASubjectToTest() {
    return this.givenASubjectToTest(mock(GenericMessageEvent.class));
  }

  private PircBotXMessageEvent givenASubjectToTest(final GenericMessageEvent genericMessageEvent) {
    return new PircBotXMessageEvent(this.testFrameworkRule.getInstance(Configuration.class),
        this.testFrameworkRule.getInstance(ChatBotConfiguration.class),
        this.testFrameworkRule.getInstance(Key.get(new TypeLiteral<ChatBotFactory<PircBotX>>() {
        })),
        this.testFrameworkRule.getInstance(Key.get(new TypeLiteral<ChatUserFactory<User>>() {
        })),
        this.testFrameworkRule.getInstance(CommandUtils.class),
        this.testFrameworkRule.getInstance(ExecutionUtils.class), genericMessageEvent);
  }

  @Test
  public final void getChatBot_whenCalled_wrapsPircBotXFromGenericMessageEvent() {
    this.installModules();
    final GenericMessageEvent genericMessageEvent = mock(GenericMessageEvent.class);
    final PircBotXMessageEvent subjectUnderTest = this.givenASubjectToTest(genericMessageEvent);

    subjectUnderTest.getChatBot();

    verify(genericMessageEvent).getBot();
  }

  @Test
  public final void getChatBot_whenCalled_returnsNonNullChatBot() {
    this.installModules();
    final GenericMessageEvent genericMessageEvent = mock(GenericMessageEvent.class);
    final PircBotXMessageEvent subjectUnderTest = this.givenASubjectToTest(genericMessageEvent);

    final ChatBot result = subjectUnderTest.getChatBot();

    assertThat(result).isNotNull();
  }

  @Test
  public final void getChatUser_whenCalled_wrapsUserFromGenericMessageEvent() {
    this.installModules();
    final GenericMessageEvent genericMessageEvent = mock(GenericMessageEvent.class);
    final PircBotXMessageEvent subjectUnderTest = this.givenASubjectToTest(genericMessageEvent);

    subjectUnderTest.getChatUser();

    verify(genericMessageEvent).getUser();
  }

  @Test
  public final void getChatUser_whenCalled_returnsNonNullChatUser() {
    this.installModules();
    final GenericMessageEvent genericMessageEvent = mock(GenericMessageEvent.class);
    final PircBotXMessageEvent subjectUnderTest = this.givenASubjectToTest(genericMessageEvent);

    final ChatUser result = subjectUnderTest.getChatUser();

    assertThat(result).isNotNull();
  }

  @Test
  public final void getMessage_whenCalled_wrapsMessageFromGenericMessageEvent() {
    this.installModules();
    final GenericMessageEvent genericMessageEvent = mock(GenericMessageEvent.class);
    when(genericMessageEvent.getMessage()).thenReturn(this.testFrameworkRule.getArbitraryString());
    final PircBotXMessageEvent subjectUnderTest = this.givenASubjectToTest(genericMessageEvent);

    subjectUnderTest.getMessage();

    verify(genericMessageEvent).getMessage();
  }

  @Test
  public final void getMessage_whenCalled_returnsNonNullMessageWithValue() {
    this.installModules();
    final String message = this.testFrameworkRule.getArbitraryString();
    final GenericMessageEvent genericMessageEvent = mock(GenericMessageEvent.class);
    when(genericMessageEvent.getMessage()).thenReturn(message);
    final PircBotXMessageEvent subjectUnderTest = this.givenASubjectToTest(genericMessageEvent);

    final Message result = subjectUnderTest.getMessage();

    final SoftAssertions softly = new SoftAssertions();
    softly.assertThat(result).isNotNull();
    softly.assertThat(result.value).isEqualTo(message);
    softly.assertAll();
  }

  @Test
  public final void sendResponse_nullMessage_throwsNullPointerExceptionWithMessage() {
    this.installModules();
    final GenericMessageEvent genericMessageEvent = mock(GenericMessageEvent.class);
    final PircBotXMessageEvent subjectUnderTest = this.givenASubjectToTest(genericMessageEvent);

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.sendResponse(null));

    this.testFrameworkRule.assertNullPointerException(thrown, "message");
  }

  @Test
  public final void sendResponse_forMessage_callsGenericMessageEventRespondWith() {
    this.installModules();
    final Message message = Message.of(this.testFrameworkRule.getArbitraryString());
    final GenericMessageEvent genericMessageEvent = mock(GenericMessageEvent.class);
    final PircBotXMessageEvent subjectUnderTest = this.givenASubjectToTest(genericMessageEvent);

    subjectUnderTest.sendResponse(message);

    verify(genericMessageEvent).respondWith(eq(String.format(RESPONSE_MESSAGE_FORMAT_DEFAULT, message)));
  }

  @Test
  public final void sendResponse_forMessage_wrapsMessageInMessageFormat() {
    this.installModules();
    final Message message = Message.of(this.testFrameworkRule.getArbitraryString());
    final GenericMessageEvent genericMessageEvent = mock(GenericMessageEvent.class);
    final PircBotXMessageEvent subjectUnderTest = this.givenASubjectToTest(genericMessageEvent);

    subjectUnderTest.sendResponse(message);

    verify(genericMessageEvent).respondWith(eq(
        String.format(RESPONSE_MESSAGE_FORMAT_DEFAULT, message.value)));
  }
}
