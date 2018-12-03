package com.mechjacktv.mechjackbot;

import java.util.Objects;
import java.util.function.Consumer;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.command.BaseCommand;
import com.mechjacktv.mechjackbot.command.CommandConfigurationBuilder;
import com.mechjacktv.mechjackbot.command.CommandMessageFormat;

public class TestCommand extends BaseCommand {

  public static final String DEFAULT_DESCRIPTION = "DEFAULT_DESCRIPTION";
  public static final String DEFAULT_MESSAGE_FORMAT = "DEFAULT_MESSAGE_FORMAT";
  public static final String DEFAULT_TRIGGER = "DEFAULT_TRIGGER";
  public static final String USAGE = "<argument>";

  private CommandDescription defaultDescription;
  private CommandMessageFormat defaultMessageFormat;
  private CommandTrigger defaultTrigger;
  private MessageEvent messageEvent;
  private Consumer<MessageEvent> messageEventHandler;
  private Boolean triggerable;
  private CommandUsage usage;

  @Inject
  public TestCommand(final CommandConfigurationBuilder commandConfigurationBuilder) {
    this(new TestCommandConfigurationBuilder(commandConfigurationBuilder));
  }

  public TestCommand(final TestCommandConfigurationBuilder testCommandConfigurationBuilder) {
    this(testCommandConfigurationBuilder.build());
  }

  public TestCommand(final TestCommandConfiguration testCommandConfiguration) {
    super(testCommandConfiguration.getCommandConfigurationBuilder());
    this.defaultDescription = testCommandConfiguration.getDefaultDescription();
    this.defaultMessageFormat = testCommandConfiguration.getDefaultMessageFormat();
    this.defaultTrigger = testCommandConfiguration.getDefaultTrigger();
    this.messageEvent = null;
    this.messageEventHandler = messageEvent -> {
      /* no-op (2018-12-02 mechjack) */
    };
    this.triggerable = null;
    this.usage = testCommandConfiguration.getUsage();
  }

  public final CommandDescription getDefaultDescription() {
    return this.defaultDescription;
  }

  public final CommandMessageFormat getDefaultMessageFormat() {
    return this.defaultMessageFormat;
  }

  public final CommandTrigger getDefaultTrigger() {
    return this.defaultTrigger;
  }

  public final MessageEvent getMessageEvent() {
    return this.messageEvent;
  }

  public final CommandUsage getUsage() {
    return this.usage;
  }

  @Override
  public void handleMessageEvent(final MessageEvent messageEvent) {
    this.messageEvent = messageEvent;
    this.messageEventHandler.accept(messageEvent);
  }

  @Override
  public boolean isTriggerable() {
    return Objects.nonNull(this.triggerable) ? this.triggerable : super.isTriggerable();
  }

  public void setMessageEventHandler(final Consumer<MessageEvent> messageEventHandler) {
    this.messageEventHandler = messageEventHandler;
  }

  public void setTriggerable(final boolean triggerable) {
    this.triggerable = triggerable;
  }

}
