package com.mechjacktv.mechjackbot;

public interface CommandUtils {

  String COMMAND_COMMAND_COOL_DOWN_KEY = "command.command_cool_down.seconds";
  String COMMAND_COMMAND_COOL_DOWN_DEFAULT = "5";
  String COMMAND_VIEWER_COOL_DOWN_KEY = "command.viewer_cool_down.seconds";
  String COMMAND_VIEWER_COOL_DOWN_DEFAULT = "15";
  String COMMAND_USAGE_MESSAGE_FORMAT_KEY = "command.usage_message_format";
  String COMMAND_USAGE_MESSAGE_FORMAT_DEFAULT = "@%s, usage: %s %s";

  boolean hasAccessLevel(Command command, MessageEvent messageEvent);

  boolean isCooledDown(Command command, MessageEvent messageEvent);

  boolean isTriggered(Command command, MessageEvent messageEvent);

  Message createUsageMessage(Command command, MessageEvent messageEvent);

  Message stripTriggerFromMessage(Command command, MessageEvent messageEvent);

}
