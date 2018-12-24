package com.mechjacktv.mechjackbot.command.custom;

import com.mechjacktv.mechjackbot.ChatCommandName;
import com.mechjacktv.mechjackbot.ChatCommandTrigger;
import com.mechjacktv.mechjackbot.ChatMessageEvent;
import com.mechjacktv.mechjackbot.UserRole;
import com.mechjacktv.mechjackbot.command.BaseChatCommand;
import com.mechjacktv.mechjackbot.command.CommandConfigurationBuilder;

// TODO (2018-12-23 mechjack): Don't extend BaseChatCommand, these shouldn't be super configurable
public class CustomChatCommand extends BaseChatCommand {

  public static final String DEFAULT_MESSAGE_FORMAT = "%2$s";

  private final CommandBody commandBody;
  private final UserRole userRole;

  protected CustomChatCommand(final CommandConfigurationBuilder commandConfigurationBuilder,
      final ChatCommandTrigger trigger, final CommandBody commandBody, final UserRole userRole) {
    super(commandConfigurationBuilder.setTrigger(trigger.value)
        .setMessageFormat(DEFAULT_MESSAGE_FORMAT));
    this.commandBody = commandBody;
    this.userRole = userRole;
  }

  @Override
  public ChatCommandName getName() {
    return ChatCommandName.of(String.format("%s#%s", super.getName(), this.getTrigger()));
  }

  @Override
  public boolean isTriggered(final ChatMessageEvent chatMessageEvent) {
    if (super.isTriggered(chatMessageEvent)) {
      if (!chatMessageEvent.getChatUser().hasUserRole(this.userRole)) {
        return false;
      }
      return true;
    }
    return false;
  }

  @Override
  public void handleMessageEvent(final ChatMessageEvent chatMessageEvent) {
    this.sendResponse(chatMessageEvent, this.commandBody);
  }

}
