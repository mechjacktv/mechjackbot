package com.mechjacktv.mechjackbot.command.core;

import static com.mechjacktv.mechjackbot.command.core.PingCommand.DEFAULT_DESCRIPTION;
import static com.mechjacktv.mechjackbot.command.core.PingCommand.DEFAULT_MESSAGE_FORMAT;
import static com.mechjacktv.mechjackbot.command.core.PingCommand.DEFAULT_TRIGGER;
import static com.mechjacktv.mechjackbot.command.core.PingCommand.KEY_DESCRIPTION;
import static com.mechjacktv.mechjackbot.command.core.PingCommand.KEY_MESSAGE_FORMAT;
import static com.mechjacktv.mechjackbot.command.core.PingCommand.KEY_TRIGGER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.mechjacktv.configuration.ConfigurationKey;
import com.mechjacktv.configuration.MapConfiguration;
import com.mechjacktv.mechjackbot.CommandDescription;
import com.mechjacktv.mechjackbot.CommandTrigger;
import com.mechjacktv.mechjackbot.Message;
import com.mechjacktv.mechjackbot.TestMessageEvent;
import com.mechjacktv.mechjackbot.command.BaseCommandContractTests;
import com.mechjacktv.mechjackbot.command.CommandConfigurationBuilder;
import com.mechjacktv.mechjackbot.command.CommandMessageFormat;

public class PingCommandUnitTests extends BaseCommandContractTests {

  @Override
  protected PingCommand givenASubjectToTest() {
    return new PingCommand(this.testFrameworkRule.getInstance(CommandConfigurationBuilder.class));
  }

  @Override
  protected CommandDescription getDescriptionDefault() {
    return CommandDescription.of(DEFAULT_DESCRIPTION);
  }

  @Override
  protected ConfigurationKey getDescriptionKey() {
    return ConfigurationKey.of(KEY_DESCRIPTION, PingCommand.class);
  }

  @Override
  protected ConfigurationKey getTriggerKey() {
    return ConfigurationKey.of(KEY_TRIGGER, PingCommand.class);
  }

  @Override
  protected CommandTrigger getTriggerDefault() {
    return CommandTrigger.of(DEFAULT_TRIGGER);
  }

  private CommandMessageFormat getMessageFormatDefault() {
    return CommandMessageFormat.of(DEFAULT_MESSAGE_FORMAT);
  }

  private ConfigurationKey getMessageFormatKey() {
    return ConfigurationKey.of(KEY_MESSAGE_FORMAT, PingCommand.class);
  }

  @Test
  public final void handleMessageEvent_noMessageFormatConfigured_resultIsDefaultMessage() {
    this.installModules();
    final TestMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestMessageEvent.class);
    final PingCommand subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.handleMessageEvent(messageEvent);
    final Message result = messageEvent.getResponseMessage();

    assertThat(result).isEqualTo(Message.of(String.format(this.getMessageFormatDefault().value,
        messageEvent.getChatUser().getTwitchLogin())));
  }

  @Test
  public final void handleMessageEvent_customMessageFormatConfigured_resultIsCustomMessage() {
    this.installModules();
    final String customMessageFormat = this.testFrameworkRule.getArbitraryString() + "%s";
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(this.getMessageFormatKey(), customMessageFormat);
    final TestMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestMessageEvent.class);
    final PingCommand subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.handleMessageEvent(messageEvent);
    final Message result = messageEvent.getResponseMessage();

    assertThat(result).isEqualTo(Message.of(String.format(customMessageFormat,
        messageEvent.getChatUser().getTwitchLogin())));
  }

}
