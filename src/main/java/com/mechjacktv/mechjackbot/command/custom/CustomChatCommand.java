package com.mechjacktv.mechjackbot.command.custom;

import com.mechjacktv.mechjackbot.ChatCommand;
import com.mechjacktv.mechjackbot.ChatCommandDescription;
import com.mechjacktv.mechjackbot.ChatCommandName;
import com.mechjacktv.mechjackbot.ChatCommandTrigger;
import com.mechjacktv.mechjackbot.ChatCommandUsage;
import com.mechjacktv.mechjackbot.ChatCommandUtils;
import com.mechjacktv.mechjackbot.ChatMessage;
import com.mechjacktv.mechjackbot.ChatMessageEvent;
import com.mechjacktv.mechjackbot.UserRole;

public class CustomChatCommand implements ChatCommand {

  private final ChatCommandUtils chatCommandUtils;
  private final ChatCommandTrigger trigger;
  private final ChatCommandDescription description;
  private final ChatCommandUsage usage;
  private final CommandBody commandBody;
  private final UserRole userRole;

  protected CustomChatCommand(final ChatCommandUtils chatCommandUtils, final ChatCommandTrigger trigger,
      final CommandBody commandBody, final UserRole userRole) {
    this.chatCommandUtils = chatCommandUtils;
    this.trigger = trigger;
    this.description = ChatCommandDescription.of("This is a custom command.");
    this.usage = ChatCommandUsage.of(String.format("%s %s", this.getTrigger(), "[<argument>...]"));
    this.commandBody = commandBody;
    this.userRole = userRole;
  }

  @Override
  public ChatCommandName getName() {
    return ChatCommandName.of(String.format("%s#%s", CustomChatCommand.class.getCanonicalName(), this.getTrigger()));
  }

  @Override
  public ChatCommandDescription getDescription() {
    return this.description;
  }

  @Override
  public ChatCommandUsage getUsage() {
    return this.usage;
  }

  @Override
  public boolean isTriggerable() {
    return true;
  }

  @Override
  public ChatCommandTrigger getTrigger() {
    return this.trigger;
  }

  @Override
  public boolean isTriggered(final ChatMessageEvent chatMessageEvent) {
    if (this.chatCommandUtils.isTriggered(this, chatMessageEvent)) {
      return chatMessageEvent.getChatUser().hasUserRole(this.userRole);
    }
    return false;
  }

  @Override
  public void handleMessageEvent(final ChatMessageEvent chatMessageEvent) {
    chatMessageEvent.sendResponse(ChatMessage.of(this.commandBody.value));
  }

}
