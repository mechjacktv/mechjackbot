package com.mechjacktv.mechjackbot.command;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.mechjacktv.mechjackbot.AppConfiguration;
import com.mechjacktv.mechjackbot.ChatBotConfiguration;
import com.mechjacktv.mechjackbot.CommandUtils;
import com.mechjacktv.mechjackbot.TwitchChannel;
import com.mechjacktv.test.ArbitraryDataGenerator;
import com.mechjacktv.util.DefaultTimeUtils;
import com.mechjacktv.util.TimeUtils;

public class AbstractCommandTestUtils {

  private final ArbitraryDataGenerator arbitraryDataGenerator;

  public AbstractCommandTestUtils(final ArbitraryDataGenerator arbitraryDataGenerator) {
    this.arbitraryDataGenerator = arbitraryDataGenerator;
  }

  public final CommandUtils givenACommandUtils(final AppConfiguration appConfiguration) {
    final ChatBotConfiguration chatBotConfiguration = this.givenAFakeChatBotConfiguration();
    final TimeUtils timeUtils = new DefaultTimeUtils();

    return new DefaultCommandUtils(appConfiguration, chatBotConfiguration, timeUtils);
  }

  private ChatBotConfiguration givenAFakeChatBotConfiguration() {
    final ChatBotConfiguration chatBotConfiguration = mock(ChatBotConfiguration.class);

    when(chatBotConfiguration.getTwitchChannel()).thenReturn(TwitchChannel.of(this.arbitraryDataGenerator.getString()));
    return chatBotConfiguration;
  }

}
