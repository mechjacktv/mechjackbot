package tv.mechjack.mechjackbot.chatbot.kicl;

import javax.inject.Inject;

import org.kitteh.irc.client.library.element.User;

import tv.mechjack.mechjackbot.ChatBotConfiguration;
import tv.mechjack.mechjackbot.ChatUser;

public class KiclChatUserFactory {

  private final ChatBotConfiguration chatBotConfiguration;

  @Inject
  KiclChatUserFactory(final ChatBotConfiguration chatBotConfiguration) {
    this.chatBotConfiguration = chatBotConfiguration;
  }

  public ChatUser create(final User user) {
    return new KiclChatUser(this.chatBotConfiguration, user);
  }

}
