package tv.mechjack.mechjackbot.api;

import java.util.Objects;

public class TestCommandConfigurationBuilder {

  private final CommandConfigurationBuilder commandConfigurationBuilder;
  private ChatCommandDescription defaultDescription;
  private CommandMessageFormat defaultMessageFormat;
  private ChatCommandTrigger defaultTrigger;
  private ChatCommandUsage usage;

  public TestCommandConfigurationBuilder(final CommandConfigurationBuilder commandConfigurationBuilder) {
    this.commandConfigurationBuilder = commandConfigurationBuilder;
    this.defaultDescription = null;
    this.defaultMessageFormat = null;
    this.defaultTrigger = null;
    this.usage = null;
  }

  public final TestCommandConfigurationBuilder setDefaultDescription(final String defaultDescription) {
    this.defaultDescription = ChatCommandDescription.of(defaultDescription);
    return this;
  }

  public final TestCommandConfigurationBuilder setDefaultMessageFormat(final String defaultMessageFormat) {
    this.defaultMessageFormat = CommandMessageFormat.of(defaultMessageFormat);
    return this;
  }

  public final TestCommandConfigurationBuilder setDefaultTrigger(final String defaultTrigger) {
    this.defaultTrigger = ChatCommandTrigger.of(defaultTrigger);
    return this;
  }

  public final TestCommandConfigurationBuilder setUsage(final String usage) {
    this.usage = ChatCommandUsage.of(usage);
    return this;
  }

  public final TestCommandConfiguration build() {
    final ChatCommandDescription defaultDescription = this.getDefaultDescription();
    final CommandMessageFormat defaultMessageFormat = this.getDefaultMessageFormat();
    final ChatCommandTrigger defaultTrigger = this.getDefaultTrigger();
    final ChatCommandUsage usage = this.getUsage();

    return new TestCommandConfiguration(this.commandConfigurationBuilder, defaultDescription, defaultMessageFormat,
        defaultTrigger, usage);
  }

  private ChatCommandDescription getDefaultDescription() {
    return Objects.isNull(this.defaultDescription)
        ? ChatCommandDescription.of(TestChatCommand.DEFAULT_DESCRIPTION)
        : this.defaultDescription;
  }

  private CommandMessageFormat getDefaultMessageFormat() {
    return Objects.isNull(this.defaultMessageFormat)
        ? CommandMessageFormat.of(TestChatCommand.DEFAULT_MESSAGE_FORMAT)
        : this.defaultMessageFormat;
  }

  private ChatCommandTrigger getDefaultTrigger() {
    return Objects.isNull(this.defaultTrigger)
        ? ChatCommandTrigger.of(TestChatCommand.DEFAULT_TRIGGER)
        : this.defaultTrigger;
  }

  private ChatCommandUsage getUsage() {
    return Objects.isNull(this.usage) ? ChatCommandUsage.of(TestChatCommand.USAGE) : this.usage;
  }

}
