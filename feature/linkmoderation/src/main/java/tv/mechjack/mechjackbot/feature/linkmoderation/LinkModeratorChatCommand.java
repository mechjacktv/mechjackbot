package tv.mechjack.mechjackbot.feature.linkmoderation;

import java.util.regex.Pattern;

import javax.inject.Inject;

import tv.mechjack.mechjackbot.api.BaseChatCommand;
import tv.mechjack.mechjackbot.api.ChatMessageEvent;
import tv.mechjack.mechjackbot.api.ChatUser;
import tv.mechjack.mechjackbot.api.CommandConfigurationBuilder;
import tv.mechjack.mechjackbot.api.CommandMessageFormat;
import tv.mechjack.mechjackbot.api.NoCoolDown;
import tv.mechjack.mechjackbot.api.RequiresAccessLevel;
import tv.mechjack.mechjackbot.api.UserRole;

public final class LinkModeratorChatCommand extends BaseChatCommand {

  public static final String URL_REGEX =
      "(?i)\\b((\\w+://)?(sub\\.)*([\\S]+\\.("
          + new TopLevelDomainList()
          + "))|\\w+://localhost)(:\\d+)?(/\\S+)?\\b";
  public static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);

  public static final String DEFAULT_DESCRIPTION = "Monitors chat for links and takes moderator action for non-subscribers.";
  public static final String DEFAULT_MESSAGE_FORMAT = "@$(user): Ask permission to post links";

  private final LinkModeratorService linkModeratorService;

  @Inject
  LinkModeratorChatCommand(
      final CommandConfigurationBuilder commandConfigurationBuilder,
      final LinkModeratorService linkModeratorService) {
    super(commandConfigurationBuilder.setDescription(DEFAULT_DESCRIPTION)
        .setMessageFormat(DEFAULT_MESSAGE_FORMAT));
    this.linkModeratorService = linkModeratorService;
  }

  @Override
  public final boolean isTriggered(final ChatMessageEvent chatMessageEvent) {
    if (!this.linkModeratorService.hasPermit(chatMessageEvent.getChatUser())) {
      return URL_PATTERN.matcher(chatMessageEvent.getChatMessage().value)
          .find();
    }
    return false;
  }

  @Override
  @NoCoolDown
  @RequiresAccessLevel(UserRole.VIEWER)
  public void handleMessageEvent(ChatMessageEvent chatMessageEvent) {
    final ChatUser chatUser = chatMessageEvent.getChatUser();

    this.sendRawResponse(chatMessageEvent, CommandMessageFormat.of(
        "/timeout @$(user) "
            + this.linkModeratorService.timeoutDuration(chatUser)
            + " Ask permission to post links"));
    this.sendResponse(chatMessageEvent);
  }

}
