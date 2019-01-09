package tv.mechjack.mechjackbot.chatbot;

import javax.inject.Inject;

import tv.mechjack.application.ApplicationDataLocation;
import tv.mechjack.mechjackbot.ChatBotConfiguration;
import tv.mechjack.mechjackbot.ChatChannel;
import tv.mechjack.mechjackbot.UserPassword;
import tv.mechjack.testframework.ArbitraryDataGenerator;
import tv.mechjack.twitchclient.TwitchLogin;

public class TestChatBotConfiguration implements ChatBotConfiguration {

  private final ApplicationDataLocation applicationDataLocation;
  private final ChatChannel chatChannel;
  private final UserPassword userPassword;
  private final TwitchLogin twitchLogin;

  @Inject
  public TestChatBotConfiguration(final ArbitraryDataGenerator arbitraryDataGenerator) {
    this.applicationDataLocation = ApplicationDataLocation.of(arbitraryDataGenerator.getString());
    this.chatChannel = ChatChannel.of(arbitraryDataGenerator.getString());
    this.userPassword = UserPassword.of(arbitraryDataGenerator.getString());
    this.twitchLogin = TwitchLogin.of(arbitraryDataGenerator.getString());
  }

  public ApplicationDataLocation getApplicationDataLocation() {
    return this.applicationDataLocation;
  }

  @Override
  public ChatChannel getChatChannel() {
    return this.chatChannel;
  }

  @Override
  public UserPassword getUserPassword() {
    return this.userPassword;
  }

  @Override
  public TwitchLogin getTwitchLogin() {
    return this.twitchLogin;
  }
}