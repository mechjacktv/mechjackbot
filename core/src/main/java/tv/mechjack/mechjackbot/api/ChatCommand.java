package tv.mechjack.mechjackbot.api;

public interface ChatCommand {

  ChatCommandName getName();

  ChatCommandDescription getDescription();

  ChatCommandUsage getUsage();

  boolean isTriggerable();

  ChatCommandTrigger getTrigger();

  boolean isTriggered(ChatMessageEvent chatMessageEvent);

  void handleMessageEvent(ChatMessageEvent chatMessageEvent);

}
