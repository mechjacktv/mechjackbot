package com.mechjacktv.mechjackbot;

public interface CommandUtils {

  ChatUsername getSanitizedViewerName(MessageEvent messageEvent);

  boolean isChannelOwner(MessageEvent messageEvent);

  boolean isCommandTrigger(CommandTrigger commandTrigger, MessageEvent messageEvent);

  boolean isGloballyCooledDown(Command command);

  boolean isPrivilegedViewer(MessageEvent messageEvent);

  boolean isRegularViewer(MessageEvent messageEvent);

  void sendUsage(MessageEvent messageEvent, CommandUsage usage);

  String stripTriggerOffMessage(CommandTrigger trigger, Message message);

}
