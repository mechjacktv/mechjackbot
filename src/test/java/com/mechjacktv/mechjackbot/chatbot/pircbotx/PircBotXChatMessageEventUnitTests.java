package com.mechjacktv.mechjackbot.chatbot.pircbotx;

import static com.mechjacktv.mechjackbot.chatbot.pircbotx.PircBotXChatMessageEvent.RESPONSE_MESSAGE_FORMAT_DEFAULT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.assertj.core.api.SoftAssertions;
import org.junit.Rule;
import org.junit.Test;
import org.pircbotx.hooks.types.GenericMessageEvent;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.configuration.ConfigurationTestModule;
import com.mechjacktv.mechjackbot.ChatBot;
import com.mechjacktv.mechjackbot.ChatMessage;
import com.mechjacktv.mechjackbot.ChatUser;
import com.mechjacktv.mechjackbot.chatbot.ChatBotTestModule;
import com.mechjacktv.mechjackbot.command.CommandTestModule;
import com.mechjacktv.testframework.TestFrameworkRule;
import com.mechjacktv.util.ExecutionUtils;
import com.mechjacktv.util.UtilTestModule;

public class PircBotXChatMessageEventUnitTests {

  @Rule
  public final TestFrameworkRule testFrameworkRule = new TestFrameworkRule();

  private void installModules() {
    this.testFrameworkRule.installModule(new ChatBotTestModule());
    this.testFrameworkRule.installModule(new CommandTestModule());
    this.testFrameworkRule.installModule(new ConfigurationTestModule());
    this.testFrameworkRule.installModule(new PircBotXChatBotTestModule());
    this.testFrameworkRule.installModule(new UtilTestModule());
  }

  private PircBotXChatMessageEvent givenASubjectToTest() {
    return this.givenASubjectToTest(mock(GenericMessageEvent.class));
  }

  private PircBotXChatMessageEvent givenASubjectToTest(final GenericMessageEvent genericMessageEvent) {
    return new PircBotXChatMessageEvent(this.testFrameworkRule.getInstance(Configuration.class),
        this.testFrameworkRule.getInstance(PircBotXChatBotFactory.class),
        this.testFrameworkRule.getInstance(PircBotXChatUserFactory.class),
        this.testFrameworkRule.getInstance(ExecutionUtils.class), genericMessageEvent);
  }

  @Test
  public final void getChatBot_whenCalled_wrapsPircBotXFromGenericMessageEvent() {
    this.installModules();
    final GenericMessageEvent genericMessageEvent = mock(GenericMessageEvent.class);
    final PircBotXChatMessageEvent subjectUnderTest = this.givenASubjectToTest(genericMessageEvent);

    subjectUnderTest.getChatBot();

    verify(genericMessageEvent).getBot();
  }

  @Test
  public final void getChatBot_whenCalled_returnsNonNullChatBot() {
    this.installModules();
    final GenericMessageEvent genericMessageEvent = mock(GenericMessageEvent.class);
    final PircBotXChatMessageEvent subjectUnderTest = this.givenASubjectToTest(genericMessageEvent);

    final ChatBot result = subjectUnderTest.getChatBot();

    assertThat(result).isNotNull();
  }

  @Test
  public final void getChatUser_whenCalled_wrapsUserFromGenericMessageEvent() {
    this.installModules();
    final GenericMessageEvent genericMessageEvent = mock(GenericMessageEvent.class);
    final PircBotXChatMessageEvent subjectUnderTest = this.givenASubjectToTest(genericMessageEvent);

    subjectUnderTest.getChatUser();

    verify(genericMessageEvent).getUser();
  }

  @Test
  public final void getChatUser_whenCalled_returnsNonNullChatUser() {
    this.installModules();
    final GenericMessageEvent genericMessageEvent = mock(GenericMessageEvent.class);
    final PircBotXChatMessageEvent subjectUnderTest = this.givenASubjectToTest(genericMessageEvent);

    final ChatUser result = subjectUnderTest.getChatUser();

    assertThat(result).isNotNull();
  }

  @Test
  public final void getMessage_whenCalled_wrapsMessageFromGenericMessageEvent() {
    this.installModules();
    final GenericMessageEvent genericMessageEvent = mock(GenericMessageEvent.class);
    when(genericMessageEvent.getMessage()).thenReturn(this.testFrameworkRule.getArbitraryString());
    final PircBotXChatMessageEvent subjectUnderTest = this.givenASubjectToTest(genericMessageEvent);

    subjectUnderTest.getChatMessage();

    verify(genericMessageEvent).getMessage();
  }

  @Test
  public final void getMessage_whenCalled_returnsNonNullMessageWithValue() {
    this.installModules();
    final String message = this.testFrameworkRule.getArbitraryString();
    final GenericMessageEvent genericMessageEvent = mock(GenericMessageEvent.class);
    when(genericMessageEvent.getMessage()).thenReturn(message);
    final PircBotXChatMessageEvent subjectUnderTest = this.givenASubjectToTest(genericMessageEvent);

    final ChatMessage result = subjectUnderTest.getChatMessage();

    final SoftAssertions softly = new SoftAssertions();
    softly.assertThat(result).isNotNull();
    softly.assertThat(result.value).isEqualTo(message);
    softly.assertAll();
  }

  @Test
  public final void sendResponse_nullMessage_throwsNullPointerExceptionWithMessage() {
    this.installModules();
    final GenericMessageEvent genericMessageEvent = mock(GenericMessageEvent.class);
    final PircBotXChatMessageEvent subjectUnderTest = this.givenASubjectToTest(genericMessageEvent);

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.sendResponse(null));

    this.testFrameworkRule.assertNullPointerException(thrown, "chatMessage");
  }

  @Test
  public final void sendResponse_forMessage_callsGenericMessageEventRespondWith() {
    this.installModules();
    final ChatMessage chatMessage = ChatMessage.of(this.testFrameworkRule.getArbitraryString());
    final GenericMessageEvent genericMessageEvent = mock(GenericMessageEvent.class);
    final PircBotXChatMessageEvent subjectUnderTest = this.givenASubjectToTest(genericMessageEvent);

    subjectUnderTest.sendResponse(chatMessage);

    verify(genericMessageEvent).respondWith(eq(String.format(RESPONSE_MESSAGE_FORMAT_DEFAULT, chatMessage)));
  }

  @Test
  public final void sendResponse_forMessage_wrapsMessageInMessageFormat() {
    this.installModules();
    final ChatMessage chatMessage = ChatMessage.of(this.testFrameworkRule.getArbitraryString());
    final GenericMessageEvent genericMessageEvent = mock(GenericMessageEvent.class);
    final PircBotXChatMessageEvent subjectUnderTest = this.givenASubjectToTest(genericMessageEvent);

    subjectUnderTest.sendResponse(chatMessage);

    verify(genericMessageEvent).respondWith(eq(
        String.format(RESPONSE_MESSAGE_FORMAT_DEFAULT, chatMessage.value)));
  }

}
