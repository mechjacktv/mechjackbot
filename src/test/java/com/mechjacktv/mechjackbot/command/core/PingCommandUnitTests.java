package com.mechjacktv.mechjackbot.command.core;

import static com.mechjacktv.mechjackbot.command.core.PingCommand.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.junit.Test;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.configuration.MapConfiguration;
import com.mechjacktv.configuration.SettingKey;
import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.mechjackbot.command.ArbitraryCommandTestUtils;
import com.mechjacktv.mechjackbot.command.BaseCommand;
import com.mechjacktv.mechjackbot.command.BaseCommandContractTests;
import com.mechjacktv.mechjackbot.command.DefaultCommandConfigurationBuilder;
import com.mechjacktv.twitchclient.TwitchLogin;
import com.mechjacktv.util.ArbitraryDataGenerator;
import com.mechjacktv.util.DefaultExecutionUtils;
import com.mechjacktv.util.ExecutionUtils;

public class PingCommandUnitTests extends BaseCommandContractTests {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();

  private final ArbitraryCommandTestUtils commandTestUtils = new ArbitraryCommandTestUtils(this.arbitraryDataGenerator);

  private final ExecutionUtils executionUtils = new DefaultExecutionUtils();

  @Override
  protected Command givenASubjectToTest(final Configuration configuration) {
    return this.givenASubjectToTest(configuration, this.commandTestUtils.givenACommandUtils(configuration));
  }

  private Command givenASubjectToTest(final String messageFormat) {
    final Configuration configuration = this.givenAnAppConfiguration(messageFormat);

    return this.givenASubjectToTest(configuration, mock(CommandUtils.class));
  }

  private Command givenASubjectToTest(final Configuration configuration, final CommandUtils commandUtils) {
    return new PingCommand(new DefaultCommandConfigurationBuilder(commandUtils, configuration, this.executionUtils));
  }

  @Override
  protected CommandDescription getDescriptionDefault() {
    return CommandDescription.of(DESCRIPTION_DEFAULT);
  }

  @Override
  protected SettingKey getDescriptionKey() {
    return SettingKey.of(BaseCommand.DESCRIPTION_KEY, PingCommand.class);
  }

  @Override
  protected SettingKey getTriggerKey() {
    return SettingKey.of(BaseCommand.TRIGGER_KEY, PingCommand.class);
  }

  @Override
  protected CommandTrigger getTriggerDefault() {
    return CommandTrigger.of(TRIGGER_DEFAULT);
  }

  private MapConfiguration givenAnAppConfiguration(final String messageFormat) {
    final MapConfiguration appConfiguration = this.givenAConfiguration();

    appConfiguration.set(SettingKey.of(BaseCommand.MESSAGE_FORMAT_KEY, PingCommand.class).value, messageFormat);
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
