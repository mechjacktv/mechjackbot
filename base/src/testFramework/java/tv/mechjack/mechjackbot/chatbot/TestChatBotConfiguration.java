package tv.mechjack.mechjackbot.chatbot;

import javax.inject.Inject;

import tv.mechjack.mechjackbot.api.ChatBotConfiguration;
import tv.mechjack.mechjackbot.api.ChatChannelName;
import tv.mechjack.mechjackbot.api.UserPassword;
import tv.mechjack.platform.application.ApplicationDataLocation;
import tv.mechjack.testframework.ArbitraryData;
import tv.mechjack.twitchclient.TwitchLogin;

public class TestChatBotConfiguration implements ChatBotConfiguration {

  private final ApplicationDataLocation applicationDataLocation;
  private final ChatChannelName chatChannelName;
  private final UserPassword userPassword;
  private final TwitchLogin twitchLogin;

  @Inject
  public TestChatBotConfiguration(final ArbitraryData arbitraryDataGenerator) {
    this.applicationDataLocation = ApplicationDataLocation.of(arbitraryDataGenerator.getString());
    this.chatChannelName = ChatChannelName.of(arbitraryDataGenerator.getString());
    this.userPassword = UserPassword.of(arbitraryDataGenerator.getString());
    this.twitchLogin = TwitchLogin.of(arbitraryDataGenerator.getString());
  }

  public ApplicationDataLocation getApplicationDataLocation() {
    return this.applicationDataLocation;
  }

  @Override
  public ChatChannelName getChatChannelName() {
    return this.chatChannelName;
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
