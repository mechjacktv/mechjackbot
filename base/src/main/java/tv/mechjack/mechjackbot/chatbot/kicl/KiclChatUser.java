package tv.mechjack.mechjackbot.chatbot.kicl;

import java.util.List;
import java.util.Optional;

import org.kitteh.irc.client.library.element.ServerMessage;
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;

import tv.mechjack.mechjackbot.api.ChatUser;
import tv.mechjack.mechjackbot.api.UserRole;
import tv.mechjack.twitchclient.TwitchLogin;

public class KiclChatUser implements ChatUser {

  private final TwitchLogin twitchLogin;
  private final ChannelMessageEvent event;

  KiclChatUser(final ChannelMessageEvent event) {
    this.event = event;
    this.twitchLogin = TwitchLogin.of(this.event.getActor().getNick());
  }

  @Override
  public TwitchLogin getTwitchLogin() {
    return this.twitchLogin;
  }

  public UserRole getUserRole() {
    final List<ServerMessage> originalMessages = this.event.getOriginalMessages();

    if (originalMessages.size() > 0) {
      final Optional<String> optionalBadges = originalMessages.get(0).getTag("badges").get().getValue();

      if (optionalBadges.isPresent()) {
        final String badges = optionalBadges.get().toLowerCase();

        if (badges.contains("broadcaster")) {
          return UserRole.BROADCASTER;
        } else if (badges.contains("moderator")) {
          return UserRole.MODERATOR;
        } else if (badges.contains("vip")) {
          return UserRole.VIP;
        } else if (badges.contains("subscriber")) {
          return UserRole.SUBSCRIBER;
        }
      }
    }
    return UserRole.VIEWER;
  }

  @Override
  public boolean hasAccessLevel(final UserRole userRole) {
    return this.getUserRole().accessLevel() <= userRole.accessLevel();
  }

}
