package tv.mechjack.mechjackbot.feature.linkmoderation;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import com.google.common.base.Strings;

import tv.mechjack.mechjackbot.api.BaseChatCommand;
import tv.mechjack.mechjackbot.api.ChatCommandUtils;
import tv.mechjack.mechjackbot.api.ChatMessage;
import tv.mechjack.mechjackbot.api.ChatMessageEvent;
import tv.mechjack.mechjackbot.api.CommandConfigurationBuilder;
import tv.mechjack.mechjackbot.api.RequiresAccessLevel;
import tv.mechjack.mechjackbot.api.UserRole;
import tv.mechjack.twitchclient.TwitchLogin;

public class PermitChatCommand extends BaseChatCommand {

  public static final String DEFAULT_DESCRIPTION = "Permits a user to post links for 30 seconds";
  public static final String DEFAULT_MESSAGE_FORMAT = "@%s, $(user) is permitting you to post links for 30 seconds.";
  public static final String DEFAULT_TRIGGER = "!permit";
  public static final String USAGE = "<viewer>";
  // TODO (2019-02-23 mechjack): make configurable
  public static final String DURATION = "30";

  private final ChatCommandUtils chatCommandUtils;
  private final LinkModeratorService linkModeratorService;

  @Inject
  protected PermitChatCommand(
      final CommandConfigurationBuilder commandConfigurationBuilder,
      final ChatCommandUtils chatCommandUtils,
      final LinkModeratorService linkModeratorService) {
    super(commandConfigurationBuilder.setTrigger(DEFAULT_TRIGGER)
        .setDescription(DEFAULT_DESCRIPTION)
        .setMessageFormat(DEFAULT_MESSAGE_FORMAT)
        .setUsage(USAGE));
    this.chatCommandUtils = chatCommandUtils;
    this.linkModeratorService = linkModeratorService;
  }

  @Override
  @RequiresAccessLevel(UserRole.MODERATOR)
  public void handleMessageEvent(final ChatMessageEvent chatMessageEvent) {
    final ChatMessage chatMessage = this.chatCommandUtils
        .stripTriggerFromMessage(this, chatMessageEvent);

    if (!Strings.isNullOrEmpty(chatMessage.value)) {
      final TwitchLogin twitchLogin = TwitchLogin.of(chatMessage.value.trim());

      this.linkModeratorService.grantPermit(twitchLogin,
          TimeUnit.SECONDS.toMillis(Integer.parseInt(DURATION)));
      this.sendResponse(chatMessageEvent, twitchLogin);
    } else {
      this.sendUsage(chatMessageEvent);
    }
  }

}
