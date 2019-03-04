package tv.mechjack.mechjackbot.feature.autotrigger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.google.common.collect.Sets;

import picocli.CommandLine.Model.OptionSpec;
import picocli.CommandLine.Model.PositionalParamSpec;
import picocli.CommandLine.ParseResult;

import tv.mechjack.mechjackbot.api.BaseChatCommand;
import tv.mechjack.mechjackbot.api.ChatCommandTrigger;
import tv.mechjack.mechjackbot.api.ChatMessageEvent;
import tv.mechjack.mechjackbot.api.CommandConfigurationBuilder;
import tv.mechjack.mechjackbot.api.CommandMessageFormat;
import tv.mechjack.mechjackbot.api.PicoCliUtils;
import tv.mechjack.mechjackbot.api.RequiresAccessLevel;
import tv.mechjack.mechjackbot.api.UserRole;
import tv.mechjack.platform.configuration.Configuration;
import tv.mechjack.platform.configuration.ConfigurationKey;

public class AutoTriggerSetChatCommand extends BaseChatCommand {

  public static final String DEFAULT_DESCRIPTION = "Creates or updates an auto trigger list.";
  public static final String DEFAULT_MESSAGE_FORMAT = "$(user), auto trigger list set, %s";
  public static final String DEFAULT_TRIGGERS_REQUIRED_MESSAGE_FORMAT = "$(user), $(trigger) failed: triggers required";
  public static final String DEFAULT_TRIGGER = "!autoset";
  public static final ConfigurationKey KEY_TRIGGERS_REQUIRED_MESSAGE_FORMAT = ConfigurationKey.of(
      "triggers_required_message_format",
      AutoTriggerSetChatCommand.class);
  public static final String USAGE = String.format("<name> "
      + "[(-t|--time-required)=<minutes>] [(-m|--message-count)=<count>] "
      + "[(-c|--chatter-count)=<count>] [(-o|--order)=(%s)] <trigger> "
      + "[<trigger>...]",
      Arrays.stream(Order.values()).map(Object::toString).collect(
          Collectors.joining("|")));

  private final AutoTriggerService autoTriggerService;
  private final Configuration configuration;
  private final PicoCliUtils picoCliUtils;

  @Inject
  protected AutoTriggerSetChatCommand(
      final CommandConfigurationBuilder commandConfigurationBuilder,
      final AutoTriggerService autoTriggerService,
      final Configuration configuration, final PicoCliUtils picoCliUtils) {
    super(commandConfigurationBuilder.setTrigger(DEFAULT_TRIGGER)
        .setDescription(DEFAULT_DESCRIPTION)
        .setMessageFormat(DEFAULT_MESSAGE_FORMAT)
        .setUsage(USAGE));
    this.autoTriggerService = autoTriggerService;
    this.configuration = configuration;
    this.picoCliUtils = picoCliUtils;
  }

  @Override
  @RequiresAccessLevel(UserRole.MODERATOR)
  public void handleMessageEvent(final ChatMessageEvent messageEvent) {
    final OptionSpec timeRequiredOption = this.picoCliUtils.createStringOption(
        false, "-t", "--time-required");
    final OptionSpec messageCountOption = this.picoCliUtils.createStringOption(
        false, "-m", "--message-count");
    final OptionSpec chatterCountOption = this.picoCliUtils.createStringOption(
        false, "-c", "--chatter-count");
    final OptionSpec orderOption = this.picoCliUtils.createStringOption(
        false, "-o", "--order");
    final PositionalParamSpec listNameParam = this.picoCliUtils.createStringParam(true, "0");
    final PositionalParamSpec triggerParams = this.picoCliUtils.createStringListParam(false, "1..*");

    this.parseArguments(Sets.newHashSet(timeRequiredOption,
        messageCountOption, chatterCountOption, orderOption, listNameParam,
        triggerParams), messageEvent, parseResult -> {
          try {
            final ListName listName = ListName.of(listNameParam.getValue());
            final List<ChatCommandTrigger> triggers = this.handleTriggerParams(triggerParams, parseResult);
            final TimeRequired timeRequired = this.handleTimeRequiredOption(timeRequiredOption, parseResult);
            final MessageCount messageCount = this.handleMessageCountOption(messageCountOption, parseResult);
            final ChatterCount chatterCount = this.handleChatterCountOption(chatterCountOption, parseResult);
            final Order order = this.handleOrderOption(orderOption, parseResult);

            if (this.autoTriggerService.isExistingAutoTriggerList(listName)) {
              this.autoTriggerService.updateAutoTriggerList(listName,
                  timeRequired, messageCount, chatterCount, order, triggers);
            } else {
              if (Objects.isNull(triggers)) {
                this.sendResponse(messageEvent,
                    this.getTriggersRequiredMessageFormat());
                return false;
              }
              this.autoTriggerService.createAutoTriggerList(listName,
                  timeRequired, messageCount, chatterCount, order, triggers);
            }
            this.sendResponse(messageEvent, listName);
            return true;
          } catch (final IllegalArgumentException e) {
            this.sendUsage(messageEvent);
            return false;
          }
        });
  }

  private List<ChatCommandTrigger> handleTriggerParams(
      final PositionalParamSpec param, final ParseResult parseResult) {
    if (parseResult.hasMatchedPositional(param)) {
      final List<ChatCommandTrigger> commandTriggers = new ArrayList<>();
      final List<String> triggers = param.getValue();

      for (final String trigger : triggers) {
        commandTriggers.add(ChatCommandTrigger.of(trigger));
      }
      return commandTriggers.size() > 0 ? commandTriggers : null;
    }
    return null;
  }

  private TimeRequired handleTimeRequiredOption(final OptionSpec option,
      final ParseResult parseResult) {
    if (parseResult.hasMatchedOption(option)) {
      return TimeRequired.of(Integer.parseInt(option.getValue()));
    }
    return null;
  }

  private MessageCount handleMessageCountOption(final OptionSpec option,
      final ParseResult parseResult) {
    if (parseResult.hasMatchedOption(option)) {
      return MessageCount.of(Integer.parseInt(option.getValue()));
    }
    return null;
  }

  private ChatterCount handleChatterCountOption(final OptionSpec option,
      final ParseResult parseResult) {
    if (parseResult.hasMatchedOption(option)) {
      return ChatterCount.of(Integer.parseInt(option.getValue()));
    }
    return null;
  }

  private Order handleOrderOption(final OptionSpec option,
      final ParseResult parseResult) {
    if (parseResult.hasMatchedOption(option)) {
      return Order.valueOf(option.getValue());
    }
    return null;
  }

  private CommandMessageFormat getTriggersRequiredMessageFormat() {
    return CommandMessageFormat.of(
        this.configuration.get(KEY_TRIGGERS_REQUIRED_MESSAGE_FORMAT,
            DEFAULT_TRIGGERS_REQUIRED_MESSAGE_FORMAT));
  }

}
