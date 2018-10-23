package com.mechjacktv.mechjackbot.command;

import com.mechjacktv.mechjackbot.AppConfiguration;
import com.mechjacktv.mechjackbot.GlobalCoolDown;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.RestrictToOwner;

import javax.inject.Inject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("CanBeFinal")
class SetConfigCommand extends AbstractCommand {

  private static final String COMMAND_SYNTAX_REGEX = "\\s+([\\w\\d.-_]+)\\s*=\\s*(.+)";

  private final AppConfiguration appConfiguration;
  private final Pattern commandSyntaxPattern;
  private final CommandUtils commandUtils;

  @Inject
  SetConfigCommand(final AppConfiguration appConfiguration, final CommandUtils commandUtils) {
    super("!setconfig", commandUtils);
    this.appConfiguration = appConfiguration;
    this.commandSyntaxPattern = Pattern.compile(this.getTrigger() + COMMAND_SYNTAX_REGEX);
    this.commandUtils = commandUtils;
  }

  @Override
  public String getDescription() {
    return "Sets the configuration key to the value specified.";
  }

  @Override
  @RestrictToOwner
  @GlobalCoolDown
  public void handleMessage(MessageEvent messageEvent) {
    final String message = messageEvent.getMessage();
    final Matcher messageMatcher = this.commandSyntaxPattern.matcher(message);

    if (messageMatcher.matches()) {
      final String key = messageMatcher.group(1).trim().toLowerCase();
      final String value = messageMatcher.group(2).trim();

      this.appConfiguration.set(key, value);
      messageEvent.sendResponse(String.format("%s: %s", key, value));
    } else {
      this.commandUtils.sendUsage(messageEvent, String.format("%s <key> = <value>", this.getTrigger()));
    }
  }

}
