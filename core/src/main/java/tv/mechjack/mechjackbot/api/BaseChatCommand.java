package tv.mechjack.mechjackbot.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import picocli.CommandLine;
import picocli.CommandLine.IParseResultHandler2;
import picocli.CommandLine.Model.ArgSpec;
import picocli.CommandLine.Model.CommandSpec;

import tv.mechjack.configuration.Configuration;
import tv.mechjack.configuration.ConfigurationKey;
import tv.mechjack.util.ExecutionUtils;

public abstract class BaseChatCommand implements PicoCliCommandParser, RespondingChatCommand {

  public static final String KEY_DESCRIPTION = "description";
  public static final String KEY_MESSAGE_FORMAT = "message_format";
  public static final String KEY_TRIGGER = "trigger";

  public static final Pattern ARGUMENTS_PATTERN = Pattern.compile("(\"(.+)\"|(\\S+))");

  private final Configuration configuration;
  private final ChatCommandUtils chatCommandUtils;
  private final ExecutionUtils executionUtils;
  private final ChatCommandDescription descriptionDefault;
  private final ConfigurationKey descriptionKey;
  private final CommandMessageFormat messageFormatDefault;
  private final ConfigurationKey messageFormatKey;
  private final ChatCommandTrigger triggerDefault;
  private final ConfigurationKey triggerKey;
  private final ChatCommandUsage usage;
  private final boolean triggerable;

  protected BaseChatCommand(CommandConfigurationBuilder commandConfigurationBuilder) {
    this(commandConfigurationBuilder.build());
  }

  protected BaseChatCommand(CommandConfiguration commandConfiguration) {
    this.chatCommandUtils = commandConfiguration.getChatCommandUtils();
    this.configuration = commandConfiguration.getConfiguration();
    this.executionUtils = commandConfiguration.getExecutionUtils();
    this.descriptionDefault = commandConfiguration.getDescription();
    this.descriptionKey = ConfigurationKey.of(KEY_DESCRIPTION, this.getClass());
    this.messageFormatDefault = commandConfiguration.getMessageFormat();
    this.messageFormatKey = ConfigurationKey.of(KEY_MESSAGE_FORMAT, this.getClass());
    this.triggerDefault = commandConfiguration.getTrigger();
    this.triggerKey = ConfigurationKey.of(KEY_TRIGGER, this.getClass());
    this.triggerable = commandConfiguration.isTriggerable();
    this.usage = commandConfiguration.getUsage();
  }

  @Override
  public ChatCommandName getName() {
    return ChatCommandName.of(this.getClass().getCanonicalName());
  }

  @Override
  public ChatCommandDescription getDescription() {
    return ChatCommandDescription.of(this.configuration.get(this.descriptionKey.value, this.descriptionDefault.value));
  }

  @Override
  public ChatCommandUsage getUsage() {
    return this.usage;
  }

  @Override
  public boolean isTriggerable() {
    return this.triggerable;
  }

  @Override
  public final ChatCommandTrigger getTrigger() {
    return ChatCommandTrigger.of(this.configuration.get(this.triggerKey.value, this.triggerDefault.value));
  }

  @Override
  public boolean isTriggered(final ChatMessageEvent chatMessageEvent) {
    return this.chatCommandUtils.isTriggered(this, chatMessageEvent);
  }

  @Override
  public final void sendResponse(final ChatMessageEvent chatMessageEvent, final Object... args) {
    final CommandMessageFormat messageFormat = CommandMessageFormat.of(
        this.configuration.get(this.messageFormatKey.value, this.messageFormatDefault.value));

    this.sendResponse(chatMessageEvent, messageFormat, args);
  }

  @Override
  public final void sendResponse(final ChatMessageEvent chatMessageEvent, final CommandMessageFormat messageFormat,
      final Object... args) {
    Objects.requireNonNull(chatMessageEvent, this.executionUtils.nullMessageForName("chatMessageEvent"));
    Objects.requireNonNull(messageFormat, this.executionUtils.nullMessageForName("messageFormat"));

    chatMessageEvent.sendResponse(this.chatCommandUtils.replaceChatMessageVariables(this, chatMessageEvent,
        ChatMessage.of(String.format(messageFormat.value, args))));
  }

  @Override
  public final void sendUsage(final ChatMessageEvent chatMessageEvent) {
    this.sendResponse(chatMessageEvent, CommandMessageFormat.of(
        this.chatCommandUtils.createUsageMessage(this, chatMessageEvent).value));
  }

  @Override
  public boolean parseArguments(final Collection<ArgSpec> argSpecs, final ChatMessageEvent messageEvent,
      final IParseResultHandler2<Boolean> handler) {
    final CommandSpec commandSpec = CommandSpec.create();

    for (final ArgSpec argSpec : argSpecs) {
      commandSpec.add(argSpec);
    }
    return this.parseArguments(commandSpec, messageEvent, handler);
  }

  @Override
  public boolean parseArguments(final CommandSpec commandSpec, final ChatMessageEvent messageEvent,
      final IParseResultHandler2<Boolean> handler) {
    final ChatMessage cleanMessage = this.chatCommandUtils.stripTriggerFromMessage(this, messageEvent);
    final CommandLine commandLine = new CommandLine(commandSpec);

    if ("".equals(cleanMessage.value)) {
      this.sendUsage(messageEvent);
      return false;
    }

    // !setcommand !trigger -r SUBSCRIBER -d "This is a description" This is a
    // command body
    return commandLine.parseWithHandlers(handler, new ShowUsagePicoCliExceptionHandler(this, messageEvent),
        this.splitArguments(cleanMessage.value));
  }

  private String[] splitArguments(final String argumentsLine) {
    final List<String> arguments = new ArrayList<>();
    final Matcher matcher = ARGUMENTS_PATTERN.matcher(argumentsLine);

    while (matcher.find()) {
      final String argument = matcher.group();

      if (argument.startsWith("\"")) {
        arguments.add(matcher.group(2));
      } else {
        arguments.add(argument);
      }
    }
    return arguments.toArray(new String[0]);
  }

}
