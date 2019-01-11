package tv.mechjack.mechjackbot.command.custom;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.google.common.collect.Sets;

import picocli.CommandLine.Model.OptionSpec;
import picocli.CommandLine.Model.PositionalParamSpec;
import picocli.CommandLine.ParseResult;

import tv.mechjack.configuration.Configuration;
import tv.mechjack.configuration.ConfigurationKey;
import tv.mechjack.mechjackbot.api.BaseChatCommand;
import tv.mechjack.mechjackbot.api.ChatCommandDescription;
import tv.mechjack.mechjackbot.api.ChatCommandTrigger;
import tv.mechjack.mechjackbot.api.ChatMessageEvent;
import tv.mechjack.mechjackbot.api.CommandConfigurationBuilder;
import tv.mechjack.mechjackbot.api.CommandMessageFormat;
import tv.mechjack.mechjackbot.api.PicoCliUtils;
import tv.mechjack.mechjackbot.api.RequiresUserRole;
import tv.mechjack.mechjackbot.api.UserRole;

public class SetCommandChatCommand extends BaseChatCommand {

  public static final String DEFAULT_DESCRIPTION = "Set a custom command body and/or access level.";
  public static final String DEFAULT_MESSAGE_FORMAT = "$(user), command set, %s";
  public static final String DEFAULT_BODY_REQUIRED_MESSAGE_FORMAT = "$(user), $(trigger) failed: body required";
  public static final String DEFAULT_TRIGGER = "!setcommand";
  public static final ConfigurationKey KEY_BODY_REQUIRED_MESSAGE_FORMAT = ConfigurationKey
      .of("body_required_message_format", SetCommandChatCommand.class);
  public static final String USAGE = String.format("<trigger> [(-u|--user-role)=\"(%s)\"] [(-d|--description)"
      + "=\"<description>\"][<body>]",
      Arrays.stream(UserRole.values()).map(Object::toString).collect(Collectors.joining("|")));

  private final Configuration configuration;
  private final CustomChatCommandService customChatCommandService;
  private final PicoCliUtils picoCliUtils;

  @Inject
  protected SetCommandChatCommand(
      final CommandConfigurationBuilder commandConfigurationBuilder, final Configuration configuration,
      final CustomChatCommandService customChatCommandService, final PicoCliUtils picoCliUtils) {
    super(commandConfigurationBuilder.setTrigger(DEFAULT_TRIGGER)
        .setDescription(DEFAULT_DESCRIPTION)
        .setMessageFormat(DEFAULT_MESSAGE_FORMAT)
        .setUsage(USAGE));
    this.configuration = configuration;
    this.customChatCommandService = customChatCommandService;
    this.picoCliUtils = picoCliUtils;
  }

  @Override
  @RequiresUserRole(UserRole.MODERATOR)
  public void handleMessageEvent(final ChatMessageEvent messageEvent) {
    final OptionSpec userRoleOption = this.picoCliUtils.createStringOption(false, "-r", "--user-role");
    final OptionSpec descriptionOption = this.picoCliUtils.createStringOption(false, "-d", "--description");
    final PositionalParamSpec triggerParam = this.picoCliUtils.createStringParam(true, "0");
    final PositionalParamSpec bodyParam = this.picoCliUtils.createStringListParam(false, "1..*");

    this.parseArguments(Sets.newHashSet(userRoleOption, descriptionOption, triggerParam, bodyParam), messageEvent,
        parseResult -> {
          try {
            final ChatCommandTrigger trigger = ChatCommandTrigger.of(triggerParam.getValue());
            final CommandBody commandBody = this.handleBodyParam(bodyParam, parseResult);
            final ChatCommandDescription description = this.handleDescriptionOption(descriptionOption, parseResult);
            final UserRole userRole = this.handleUserRoleOption(userRoleOption, parseResult);

            if (this.customChatCommandService.isExistingCustomChatCommand(trigger)) {
              this.customChatCommandService.updateCustomChatCommand(trigger, commandBody, description, userRole);
            } else {
              if (commandBody == null) {
                this.sendResponse(messageEvent, this.getBodyRequiredMessageFormat());
                return false;
              }
              this.customChatCommandService.createCustomChatCommand(trigger, commandBody, description, userRole);
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

  private ChatCommandDescription handleDescriptionOption(final OptionSpec option, final ParseResult parseResult) {
    if (parseResult.hasMatchedOption(option)) {
      return ChatCommandDescription.of(option.getValue());
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
