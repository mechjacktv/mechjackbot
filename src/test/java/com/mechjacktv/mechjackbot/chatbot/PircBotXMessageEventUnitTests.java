package com.mechjacktv.mechjackbot.chatbot;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.pircbotx.hooks.types.GenericMessageEvent;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.mechjackbot.command.DefaultCommandUtils;
import com.mechjacktv.testframework.ArbitraryDataGenerator;
import com.mechjacktv.util.DefaultExecutionUtils;
import com.mechjacktv.util.DefaultTimeUtils;
import com.mechjacktv.util.ExecutionUtils;

public class PircBotXMessageEventUnitTests {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();
  private final ExecutionUtils executionUtils = new DefaultExecutionUtils();

  private PircBotXMessageEvent givenASubjectToTest(final GenericMessageEvent genericMessageEvent) {
    return this.givenASubjectToTest(this.givenAnAppConfiguration(), genericMessageEvent);
  }

  private PircBotXMessageEvent givenASubjectToTest(final Configuration configuration) {
    return this.givenASubjectToTest(configuration, mock(GenericMessageEvent.class));
  }

  private PircBotXMessageEvent givenASubjectToTest(final Configuration configuration,
      final GenericMessageEvent genericMessageEvent) {
    final PircBotXChatBotFactory chatBotFactory = new PircBotXChatBotFactory(configuration, this.executionUtils);
    final ChatBotConfiguration chatBotConfiguration = new ArbitraryChatBotConfiguration(this.arbitraryDataGenerator);
    final CommandUtils commandUtils = new DefaultCommandUtils(configuration, this.executionUtils,
        new DefaultTimeUtils());

    return new PircBotXMessageEvent(configuration, chatBotConfiguration, chatBotFactory, commandUtils,
        this.executionUtils, genericMessageEvent);
  }

  private Configuration givenAnAppConfiguration() {
    return this.givenAnAppConfiguration("%s");
  }

  private Configuration givenAnAppConfiguration(final String format) {
    final Configuration configuration = mock(Configuration.class);

    when(configuration.get(eq(PircBotXMessageEvent.RESPONSE_MESSAGE_FORMAT_KEY),
        eq(PircBotXMessageEvent.RESPONSE_MESSAGE_FORMAT_DEFAULT))).thenReturn(format);
    return configuration;
  }

  @Test
  public final void getChatBot_whenCalled_wrapsPircBotXFromGenericMessageEvent() {
    final GenericMessageEvent genericMessageEvent = mock(GenericMessageEvent.class);
    final PircBotXMessageEvent subjectUnderTest = this.givenASubjectToTest(genericMessageEvent);

    subjectUnderTest.getChatBot();

    verify(genericMessageEvent).getBot();
  }

  @Test
  public final void getChatBot_whenCalled_returnsNonNullChatBot() {
    final GenericMessageEvent genericMessageEvent = mock(GenericMessageEvent.class);
    final PircBotXMessageEvent subjectUnderTest = this.givenASubjectToTest(genericMessageEvent);

    final ChatBot result = subjectUnderTest.getChatBot();

    assertThat(result).isNotNull();
  }

  @Test
  public final void getChatUser_whenCalled_wrapsUserFromGenericMessageEvent() {
    final GenericMessageEvent genericMessageEvent = mock(GenericMessageEvent.class);
    final PircBotXMessageEvent subjectUnderTest = this.givenASubjectToTest(genericMessageEvent);

    subjectUnderTest.getChatUser();

    verify(genericMessageEvent).getUser();
  }

  @Test
  public final void getChatUser_whenCalled_returnsNonNullChatUser() {
    final GenericMessageEvent genericMessageEvent = mock(GenericMessageEvent.class);
    final PircBotXMessageEvent subjectUnderTest = this.givenASubjectToTest(genericMessageEvent);

    final ChatUser result = subjectUnderTest.getChatUser();

    assertThat(result).isNotNull();
  }

  @Test
  public final void getMessage_whenCalled_wrapsMessageFromGenericMessageEvent() {
    final GenericMessageEvent genericMessageEvent = mock(GenericMessageEvent.class);
    when(genericMessageEvent.getMessage()).thenReturn(this.arbitraryDataGenerator.getString());
    final PircBotXMessageEvent subjectUnderTest = this.givenASubjectToTest(genericMessageEvent);

    subjectUnderTest.getMessage();

    verify(genericMessageEvent).getMessage();
  }

  @Test
  public final void getMessage_whenCalled_returnsNonNullMessageWithValue() {
    final String message = this.arbitraryDataGenerator.getString();
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
    final GenericMessageEvent genericMessageEvent = mock(GenericMessageEvent.class);
    final PircBotXMessageEvent subjectUnderTest = this.givenASubjectToTest(genericMessageEvent);

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.sendResponse(null));

    assertThat(thrown).isInstanceOf(NullPointerException.class)
        .hasMessage(this.executionUtils.nullMessageForName("message"));
  }

  @Test
  public final void sendResponse_forMessage_callsGenericMessageEventRespondWith() {
    final Message message = Message.of(this.arbitraryDataGenerator.getString());
    final GenericMessageEvent genericMessageEvent = mock(GenericMessageEvent.class);
    final PircBotXMessageEvent subjectUnderTest = this.givenASubjectToTest(genericMessageEvent);

    subjectUnderTest.sendResponse(message);

    verify(genericMessageEvent).respondWith(eq(message.value));
  }

  @Test
  public final void sendResponse_forMessage_asksForConfiguredMessageFormat() {
    final Configuration configuration = this.givenAnAppConfiguration();
    final Message message = Message.of(this.arbitraryDataGenerator.getString());
    final PircBotXMessageEvent subjectUnderTest = this.givenASubjectToTest(configuration);

    subjectUnderTest.sendResponse(message);

    verify(configuration).get(eq(PircBotXMessageEvent.RESPONSE_MESSAGE_FORMAT_KEY),
        eq(PircBotXMessageEvent.RESPONSE_MESSAGE_FORMAT_DEFAULT));
  }

  @Test
  public final void sendResponse_forMessage_wrapsMessageInMessageFormat() {
    final Message message = Message.of(this.arbitraryDataGenerator.getString());
    final Configuration configuration = this.givenAnAppConfiguration(
        PircBotXMessageEvent.RESPONSE_MESSAGE_FORMAT_DEFAULT);
    final GenericMessageEvent genericMessageEvent = mock(GenericMessageEvent.class);
    final PircBotXMessageEvent subjectUnderTest = this.givenASubjectToTest(configuration, genericMessageEvent);

    subjectUnderTest.sendResponse(message);

    verify(genericMessageEvent).respondWith(eq(
        String.format(PircBotXMessageEvent.RESPONSE_MESSAGE_FORMAT_DEFAULT, message.value)));
  }

}
