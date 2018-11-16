package com.mechjacktv.mechjackbot.command;

import static com.mechjacktv.mechjackbot.command.PingCommand.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

import org.junit.Test;

import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.mechjackbot.configuration.MapAppConfiguration;
import com.mechjacktv.util.ArbitraryDataGenerator;

public class PingCommandUnitTests extends CommandContractTests {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();

  private final AbstractCommandTestUtils commandTestUtils = new AbstractCommandTestUtils(this.arbitraryDataGenerator);

  @Override
  protected Command givenASubjectToTest(final AppConfiguration appConfiguration) {
    return this.givenASubjectToTest(appConfiguration, this.commandTestUtils.givenACommandUtils(appConfiguration));
  }

  private Command givenASubjectToTest(final String messageFormat, final ChatUsername chatUsername) {
    final AppConfiguration appConfiguration = this.givenAnAppConfiguration(messageFormat);
    final CommandUtils commandUtils = mock(CommandUtils.class);
    when(commandUtils.sanitizeChatUsername(isA(ChatUsername.class))).thenReturn(chatUsername);

    return this.givenASubjectToTest(appConfiguration, commandUtils);
  }

  private Command givenASubjectToTest(final AppConfiguration appConfiguration, final CommandUtils commandUtils) {
    return new PingCommand(appConfiguration, commandUtils);
  }

  @Override
  protected CommandTriggerKey getCommandTriggerKey() {
    return CommandTriggerKey.of(COMMAND_TRIGGER_KEY);
  }

  @Override
  protected CommandTrigger getCommandTriggerDefault() {
    return CommandTrigger.of(COMMAND_TRIGGER_DEFAULT);
  }

  private MapAppConfiguration givenAnAppConfiguration(final String messageFormat) {
    final MapAppConfiguration appConfiguration = this.givenAnAppConfiguration();

    appConfiguration.set(COMMAND_MESSAGE_FORMAT_KEY, messageFormat);
    return appConfiguration;
  }

  private MessageEvent givenAFakeMessageEvent() {
    final MessageEvent messageEvent = mock(MessageEvent.class);
    final ChatUser chatUser = mock(ChatUser.class);

    when(messageEvent.getChatUser()).thenReturn(chatUser);
    when(chatUser.getUsername()).thenReturn(ChatUsername.of(this.arbitraryDataGenerator.getString()));
    return messageEvent;
  }

  @Test
  public final void handleMessageEvent_defaultFormat_sendsDefaultMessage() {
    final String messageFormat = COMMAND_MESSAGE_FORMAT_DEFAULT;
    final ChatUsername chatUsername = ChatUsername.of(this.arbitraryDataGenerator.getString());
    final MessageEvent messageEvent = givenAFakeMessageEvent();
    final Command subjectUnderTest = this.givenASubjectToTest(messageFormat, chatUsername);

    subjectUnderTest.handleMessageEvent(messageEvent);

    verify(messageEvent).sendResponse(eq(Message.of(String.format(messageFormat, chatUsername))));
  }

  @Test
  public final void handleMessageEvent_customFormat_sendsCustomMessage() {
    final String messageFormat = this.arbitraryDataGenerator.getString() + ", %s";
    final ChatUsername chatUsername = ChatUsername.of(this.arbitraryDataGenerator.getString());
    final MessageEvent messageEvent = givenAFakeMessageEvent();
    final Command subjectUnderTest = this.givenASubjectToTest(messageFormat, chatUsername);

    subjectUnderTest.handleMessageEvent(messageEvent);

    verify(messageEvent).sendResponse(eq(Message.of(String.format(messageFormat, chatUsername))));
  }

}
