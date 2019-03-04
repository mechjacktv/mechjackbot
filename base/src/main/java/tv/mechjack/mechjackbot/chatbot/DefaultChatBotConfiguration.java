package tv.mechjack.mechjackbot.chatbot;

import java.io.File;

import javax.inject.Inject;

import com.google.common.base.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tv.mechjack.mechjackbot.api.ChatBotConfiguration;
import tv.mechjack.mechjackbot.api.ChatChannelName;
import tv.mechjack.mechjackbot.api.UserPassword;
import tv.mechjack.platform.application.Application;
import tv.mechjack.platform.utils.scheduleservice.FileHotUpdatePropertiesSource;
import tv.mechjack.platform.utils.scheduleservice.HotUpdateProperties;
import tv.mechjack.platform.utils.scheduleservice.HotUpdatePropertiesSource;
import tv.mechjack.platform.utils.scheduleservice.ScheduleService;
import tv.mechjack.twitchclient.TwitchClientConfiguration;
import tv.mechjack.twitchclient.TwitchClientId;
import tv.mechjack.twitchclient.TwitchLogin;

public final class DefaultChatBotConfiguration implements ChatBotConfiguration, TwitchClientConfiguration {

  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultChatBotConfiguration.class);

  private static final String CONFIG_PROPERTIES_FILE_NAME = "chat_bot.config";

  static final String TWITCH_CHANNEL_KEY = "twitch.channel";
  static final String TWITCH_CLIENT_ID_KEY = "twitch.client_id";
  static final String TWITCH_PASSWORD_KEY = "twitch.password";
  static final String TWITCH_LOGIN_KEY = "twitch.login";

  private final HotUpdateProperties hotUpdateProperties;

  @Inject
  DefaultChatBotConfiguration(final Application application, final ScheduleService scheduleService) {
    this(application, new FileHotUpdatePropertiesSource(new File(application.getApplicationDataLocation().value,
        CONFIG_PROPERTIES_FILE_NAME)), scheduleService);
  }

  DefaultChatBotConfiguration(final Application application, final HotUpdatePropertiesSource hotUpdatePropertiesSource,
      final ScheduleService scheduleService) {
    this.hotUpdateProperties = new HotUpdateProperties(hotUpdatePropertiesSource, scheduleService, LOGGER);
    if (this.isMissingRequiredValues()) {
      throw new IllegalStateException(String.format("Please complete your chat bot configuration (%s)",
          new File(new File(application.getApplicationDataLocation().value), CONFIG_PROPERTIES_FILE_NAME).getPath()));
    }
  }

  private boolean isMissingRequiredValues() {
    return Strings.isNullOrEmpty(this.hotUpdateProperties.getProperty(TWITCH_LOGIN_KEY))
        || Strings.isNullOrEmpty(this.hotUpdateProperties.getProperty(TWITCH_PASSWORD_KEY))
        || Strings.isNullOrEmpty(this.hotUpdateProperties.getProperty(TWITCH_CHANNEL_KEY))
        || Strings.isNullOrEmpty(this.hotUpdateProperties.getProperty(TWITCH_CLIENT_ID_KEY));
  }

  @Override
  public ChatChannelName getChatChannelName() {
    return ChatChannelName.of(this.hotUpdateProperties.getProperty(TWITCH_CHANNEL_KEY));
  }

  @Override
  public TwitchClientId getTwitchClientId() {
    return TwitchClientId.of(this.hotUpdateProperties.getProperty(TWITCH_CLIENT_ID_KEY));
  }

  @Override
  public UserPassword getUserPassword() {
    return UserPassword.of(this.hotUpdateProperties.getProperty(TWITCH_PASSWORD_KEY));
  }

  @Override
  public TwitchLogin getTwitchLogin() {
    return TwitchLogin.of(this.hotUpdateProperties.getProperty(TWITCH_LOGIN_KEY));
  }

}
