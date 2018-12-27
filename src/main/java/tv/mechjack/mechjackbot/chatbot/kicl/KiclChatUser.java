package tv.mechjack.mechjackbot.chatbot.kicl;

import org.kitteh.irc.client.library.element.User;

import tv.mechjack.mechjackbot.ChatBotConfiguration;
import tv.mechjack.mechjackbot.ChatUser;
import tv.mechjack.mechjackbot.UserRole;
import tv.mechjack.twitchclient.TwitchLogin;

public class KiclChatUser implements ChatUser {

  private final ChatBotConfiguration chatBotConfiguration;
  private final User user;

  KiclChatUser(final ChatBotConfiguration chatBotConfiguration, final User user) {
    this.chatBotConfiguration = chatBotConfiguration;
    this.user = user;
  }

  @Override
  public TwitchLogin getTwitchLogin() {
    return TwitchLogin.of(this.user.getNick());
  }

  @Override
  public boolean hasUserRole(final UserRole userRole) {
    return UserRole.VIEWER.equals(userRole) || this.chatBotConfiguration.getChatChannel().value
        .equalsIgnoreCase(this.getTwitchLogin().value);
  }

}
