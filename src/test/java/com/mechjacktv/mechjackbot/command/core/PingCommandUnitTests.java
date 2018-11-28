package com.mechjacktv.mechjackbot.command.core;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.configuration.MapConfiguration;
import com.mechjacktv.configuration.SettingKey;
import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.mechjackbot.command.AbstractCommand;
import com.mechjacktv.mechjackbot.command.ArbitraryCommandTestUtils;
import com.mechjacktv.mechjackbot.command.DefaultCommandConfigurationBuilder;
import com.mechjacktv.twitchclient.TwitchLogin;
import com.mechjacktv.util.ArbitraryDataGenerator;

import org.junit.Test;

import static com.mechjacktv.mechjackbot.command.core.PingCommand.MESSAGE_FORMAT_DEFAULT;
import static com.mechjacktv.mechjackbot.command.core.PingCommand.TRIGGER_DEFAULT;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class PingCommandUnitTests extends CommandContractTests {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();

  private final ArbitraryCommandTestUtils commandTestUtils = new ArbitraryCommandTestUtils(this.arbitraryDataGenerator);

  @Override
  protected Command givenASubjectToTest(final Configuration configuration) {
    return this.givenASubjectToTest(configuration, this.commandTestUtils.givenACommandUtils(configuration));
  }

  private Command givenASubjectToTest(final String messageFormat) {
    final Configuration configuration = this.givenAnAppConfiguration(messageFormat);

    return this.givenASubjectToTest(configuration, mock(CommandUtils.class));
  }

  private Command givenASubjectToTest(final Configuration configuration, final CommandUtils commandUtils) {
    return new PingCommand(new DefaultCommandConfigurationBuilder(commandUtils, configuration));
  }

  @Override
  protected SettingKey getCommandTriggerKey() {
    return SettingKey.of(PingCommand.class, AbstractCommand.TRIGGER_KEY);
  }

  @Override
  protected CommandTrigger getCommandTriggerDefault() {
    return CommandTrigger.of(TRIGGER_DEFAULT);
  }

  private MapConfiguration givenAnAppConfiguration(final String messageFormat) {
    final MapConfiguration appConfiguration = this.givenAnAppConfiguration();

    appConfiguration.set(SettingKey.of(PingCommand.class, AbstractCommand.MESSAGE_FORMAT_KEY).value, messageFormat);
    return appConfiguration;
  }

  private MessageEvent givenAFakeMessageEvent(final TwitchLogin twitchLogin) {
    final MessageEvent messageEvent = mock(MessageEvent.class);
    final ChatUser chatUser = mock(ChatUser.class);

    when(messageEvent.getChatUser()).thenReturn(chatUser);
    when(chatUser.getTwitchLogin()).thenReturn(twitchLogin);
    return messageEvent;
  }

  @Test
  public final void handleMessageEvent_defaultFormat_sendsDefaultMessage() {
    final String messageFormat = MESSAGE_FORMAT_DEFAULT;
    final TwitchLogin twitchLogin = TwitchLogin.of(this.arbitraryDataGenerator.getString());
    final MessageEvent messageEvent = this.givenAFakeMessageEvent(twitchLogin);
    final Command subjectUnderTest = this.givenASubjectToTest(messageFormat);

    subjectUnderTest.handleMessageEvent(messageEvent);

    verify(messageEvent).sendResponse(eq(Message.of(String.format(messageFormat, twitchLogin))));
  }

  @Test
  public final void handleMessageEvent_customFormat_sendsCustomMessage() {
    final String messageFormat = this.arbitraryDataGenerator.getString() + ", %s";
    final TwitchLogin twitchLogin = TwitchLogin.of(this.arbitraryDataGenerator.getString());
    final MessageEvent messageEvent = this.givenAFakeMessageEvent(twitchLogin);
    final Command subjectUnderTest = this.givenASubjectToTest(messageFormat);

    subjectUnderTest.handleMessageEvent(messageEvent);

    verify(messageEvent).sendResponse(eq(Message.of(String.format(messageFormat, twitchLogin))));
  }

}