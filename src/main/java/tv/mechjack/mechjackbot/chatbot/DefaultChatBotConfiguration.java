package tv.mechjack.mechjackbot.chatbot;

import java.io.File;

import javax.inject.Inject;

import com.google.common.base.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tv.mechjack.mechjackbot.ChatBotConfiguration;
import tv.mechjack.mechjackbot.ChatChannel;
import tv.mechjack.mechjackbot.DataLocation;
import tv.mechjack.mechjackbot.UserPassword;
import tv.mechjack.twitchclient.TwitchClientConfiguration;
import tv.mechjack.twitchclient.TwitchClientId;
import tv.mechjack.twitchclient.TwitchLogin;
import tv.mechjack.util.FilePropertiesSource;
import tv.mechjack.util.HotUpdatePropertiesWrapper;
import tv.mechjack.util.PropertiesSource;
import tv.mechjack.util.scheduleservice.ScheduleService;

public final class DefaultChatBotConfiguration extends HotUpdatePropertiesWrapper
    implements ChatBotConfiguration, TwitchClientConfiguration {

  private static final Logger log = LoggerFactory.getLogger(DefaultChatBotConfiguration.class);

  private static final String DATA_LOCATION_KEY = "mechjackbot.data_location";
  private static final String DATA_LOCATION_DEFAULT = System.getProperty("user.home") + "/.mechjackbot";
  private static final String CONFIG_PROPERTIES_FILE_NAME = "chat_bot.config";

  static final String TWITCH_CHANNEL_KEY = "twitch.channel";
  static final String TWITCH_CLIENT_ID_KEY = "twitch.client_id";
  static final String TWITCH_PASSWORD_KEY = "twitch.password";
  static final String TWITCH_LOGIN_KEY = "twitch.login";

  private final DataLocation dataLocation;

  @Inject
  DefaultChatBotConfiguration(final ScheduleService scheduleService) {
    this(System.getProperty(DATA_LOCATION_KEY, DATA_LOCATION_DEFAULT), new FilePropertiesSource(
        new File(System.getProperty(DATA_LOCATION_KEY, DATA_LOCATION_DEFAULT), CONFIG_PROPERTIES_FILE_NAME)),
        scheduleService);
  }

  DefaultChatBotConfiguration(final String dataLocation, final PropertiesSource propertiesSource,
      final ScheduleService scheduleService) {
    super(propertiesSource, scheduleService, log);
    if (this.isMissingRequiredValues()) {
      throw new IllegalStateException(String.format("Please complete your chat bot configuration (%s)",
          new File(new File(dataLocation), CONFIG_PROPERTIES_FILE_NAME).getPath()));
    }
    this.dataLocation = DataLocation.of(dataLocation);
  }

  private boolean isMissingRequiredValues() {
    return Strings.isNullOrEmpty(this.getProperties().getProperty(TWITCH_LOGIN_KEY))
        || Strings.isNullOrEmpty(this.getProperties().getProperty(TWITCH_PASSWORD_KEY))
        || Strings.isNullOrEmpty(this.getProperties().getProperty(TWITCH_CHANNEL_KEY))
        || Strings.isNullOrEmpty(this.getProperties().getProperty(TWITCH_CLIENT_ID_KEY));
  }

  @Override
  public DataLocation getDataLocation() {
    return this.dataLocation;
  }

  @Override
  public ChatChannel getChatChannel() {
    return ChatChannel.of(this.getProperties().getProperty(TWITCH_CHANNEL_KEY));
  }

  @Override
  public TwitchClientId getTwitchClientId() {
    return TwitchClientId.of(this.getProperties().getProperty(TWITCH_CLIENT_ID_KEY));
  }

  @Override
  public UserPassword getUserPassword() {
    return UserPassword.of(this.getProperties().getProperty(TWITCH_PASSWORD_KEY));
  }

  @Override
  public TwitchLogin getTwitchLogin() {
    return TwitchLogin.of(this.getProperties().getProperty(TWITCH_LOGIN_KEY));
  }

}
