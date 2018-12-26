package com.mechjacktv.mechjackbot.command.custom;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.google.common.collect.Sets;

import picocli.CommandLine.Model.OptionSpec;
import picocli.CommandLine.Model.PositionalParamSpec;
import picocli.CommandLine.ParseResult;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.configuration.ConfigurationKey;
import com.mechjacktv.mechjackbot.ChatCommandTrigger;
import com.mechjacktv.mechjackbot.ChatMessageEvent;
import com.mechjacktv.mechjackbot.RequiresUserRole;
import com.mechjacktv.mechjackbot.UserRole;
import com.mechjacktv.mechjackbot.command.BaseChatCommand;
import com.mechjacktv.mechjackbot.command.CommandConfigurationBuilder;
import com.mechjacktv.mechjackbot.command.CommandMessageFormat;
import com.mechjacktv.mechjackbot.command.PicoCliUtil;

public class SetCommandChatCommand extends BaseChatCommand {

  public static final String DEFAULT_DESCRIPTION = "Set a custom command body and/or access level.";
  public static final String DEFAULT_MESSAGE_FORMAT = "$(user), command set, %s";
  public static final String DEFAULT_BODY_REQUIRED_MESSAGE_FORMAT = "$(user), $(trigger) failed: body required";
  public static final String DEFAULT_TRIGGER = "!setcommand";
  public static final ConfigurationKey KEY_BODY_REQUIRED_MESSAGE_FORMAT = ConfigurationKey
      .of("body_required_message_format", SetCommandChatCommand.class);
  public static final String USAGE = String.format("<trigger> [(-u|--user-role)=(%s)] [<body>]",
      Arrays.stream(UserRole.values()).map(Object::toString).collect(Collectors.joining("|")));

  public static final String KEY_USER_ROLE_SPEC = "KEY_USER_ROLE_SPEC";
  public static final String KEY_TRIGGER_SPEC = "KEY_TRIGGER_SPEC";
  public static final String KEY_BODY_SPEC = "KEY_BODY_SPEC";

  private final Configuration configuration;
  private final CustomChatCommandService customChatCommandService;

  @Inject
  protected SetCommandChatCommand(
      final CommandConfigurationBuilder commandConfigurationBuilder, final Configuration configuration,
      final CustomChatCommandService customChatCommandService) {
    super(commandConfigurationBuilder.setTrigger(DEFAULT_TRIGGER)
        .setDescription(DEFAULT_DESCRIPTION)
        .setMessageFormat(DEFAULT_MESSAGE_FORMAT)
        .setUsage(USAGE));
    this.configuration = configuration;
    this.customChatCommandService = customChatCommandService;
  }

  @Override
  @RequiresUserRole(UserRole.MODERATOR)
  public void handleMessageEvent(final ChatMessageEvent messageEvent) {
    final OptionSpec userRoleOption = PicoCliUtil.createStringOption(false, "-r", "--user-role");
    final PositionalParamSpec triggerParam = PicoCliUtil.createStringParam(true, "0");
    final PositionalParamSpec bodyParam = PicoCliUtil.createStringListParam(false, "1..*");

    this.parseArguments(Sets.newHashSet(userRoleOption, triggerParam, bodyParam), messageEvent, parseResult -> {
      try {
        final ChatCommandTrigger trigger = ChatCommandTrigger.of(triggerParam.getValue());
        final CommandBody commandBody = this.handleBodyParam(bodyParam, parseResult);
        final UserRole userRole = this.handleUserRoleOption(userRoleOption, parseResult);

        if (this.customChatCommandService.isExistingCustomChatCommand(trigger)) {
          this.customChatCommandService.updateCustomChatCommand(trigger, commandBody, userRole);
        } else {
          if (commandBody == null) {
            this.sendResponse(messageEvent, this.getBodyRequiredMessageFormat());
            return false;
          }
          this.customChatCommandService.createCustomChatCommand(trigger, commandBody, userRole);
        }
        this.sendResponse(messageEvent, trigger);
        return true;
      } catch (final IllegalArgumentException e) {
        this.sendUsage(messageEvent);
        return false;
      }
    });
  }

  private UserRole handleUserRoleOption(final OptionSpec option, final ParseResult parseResult) {
    if (parseResult.hasMatchedOption(option)) {
      return UserRole.valueOf(option.getValue());
    }
    return null;
  }

  private CommandBody handleBodyParam(final PositionalParamSpec param, final ParseResult parseResult) {
    if (parseResult.hasMatchedPositional(param)) {
      final List<String> bodyParts = param.getValue();

      return CommandBody.of(String.join(" ", bodyParts));
    }
    return null;
  }

  private CommandMessageFormat getBodyRequiredMessageFormat() {
    return CommandMessageFormat.of(this.configuration.get(KEY_BODY_REQUIRED_MESSAGE_FORMAT,
        DEFAULT_BODY_REQUIRED_MESSAGE_FORMAT));
  }

}
