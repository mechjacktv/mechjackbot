package tv.mechjack.mechjackbot;

import java.util.Objects;
import java.util.function.Consumer;

import javax.inject.Inject;

import tv.mechjack.mechjackbot.command.BaseChatCommand;
import tv.mechjack.mechjackbot.command.CommandConfigurationBuilder;
import tv.mechjack.mechjackbot.command.CommandMessageFormat;

public class TestChatCommand extends BaseChatCommand {

  public static final String DEFAULT_DESCRIPTION = "DEFAULT_DESCRIPTION";
  public static final String DEFAULT_MESSAGE_FORMAT = "DEFAULT_MESSAGE_FORMAT";
  public static final String DEFAULT_TRIGGER = "DEFAULT_TRIGGER";
  public static final String USAGE = "<argument>";

  private ChatCommandDescription defaultDescription;
  private CommandMessageFormat defaultMessageFormat;
  private ChatCommandTrigger defaultTrigger;
  private ChatMessageEvent chatMessageEvent;
  private Consumer<ChatMessageEvent> messageEventHandler;
  private Boolean triggerable;
  private Boolean triggered;
  private ChatCommandUsage usage;

  @Inject
  public TestChatCommand(final CommandConfigurationBuilder commandConfigurationBuilder) {
    this(new TestCommandConfigurationBuilder(commandConfigurationBuilder));
  }

  public TestChatCommand(final TestCommandConfigurationBuilder testCommandConfigurationBuilder) {
    this(testCommandConfigurationBuilder.build());
  }

  public TestChatCommand(final TestCommandConfiguration testCommandConfiguration) {
    super(testCommandConfiguration.getCommandConfigurationBuilder());
    this.defaultDescription = testCommandConfiguration.getDefaultDescription();
    this.defaultMessageFormat = testCommandConfiguration.getDefaultMessageFormat();
    this.defaultTrigger = testCommandConfiguration.getDefaultTrigger();
    this.chatMessageEvent = null;
    this.messageEventHandler = messageEvent -> {
      /* no-op (2018-12-02 mechjack) */
    };
    this.triggerable = null;
    this.triggered = null;
    this.usage = testCommandConfiguration.getUsage();
  }

  public final ChatCommandDescription getDefaultDescription() {
    return this.defaultDescription;
  }

  public final CommandMessageFormat getDefaultMessageFormat() {
    return this.defaultMessageFormat;
  }

  public final ChatCommandTrigger getDefaultTrigger() {
    return this.defaultTrigger;
  }

  public final ChatMessageEvent getChatMessageEvent() {
    return this.chatMessageEvent;
  }

  public final ChatCommandUsage getUsage() {
    return this.usage;
  }

  @Override
  public void handleMessageEvent(final ChatMessageEvent chatMessageEvent) {
    this.chatMessageEvent = chatMessageEvent;
    this.messageEventHandler.accept(chatMessageEvent);
  }

  @Override
  public boolean isTriggerable() {
    return Objects.nonNull(this.triggerable) ? this.triggerable : super.isTriggerable();
  }

  @Override
  public boolean isTriggered(final ChatMessageEvent chatMessageEvent) {
    return Objects.nonNull(this.triggered) ? this.triggered : super.isTriggered(chatMessageEvent);
  }

  public void setMessageEventHandler(final Consumer<ChatMessageEvent> messageEventHandler) {
    this.messageEventHandler = messageEventHandler;
  }

  public void setTriggerable(final boolean triggerable) {
    this.triggerable = triggerable;
  }

  public void setTriggered(final boolean triggered) {
    this.triggered = triggered;
  }

}
