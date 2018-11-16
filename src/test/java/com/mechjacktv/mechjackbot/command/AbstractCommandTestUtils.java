package com.mechjacktv.mechjackbot.command;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.mechjacktv.mechjackbot.AppConfiguration;
import com.mechjacktv.mechjackbot.ChatBotConfiguration;
import com.mechjacktv.mechjackbot.CommandUtils;
import com.mechjacktv.mechjackbot.TwitchChannel;
import com.mechjacktv.util.ArbitraryDataGenerator;
import com.mechjacktv.util.DefaultExecutionUtils;
import com.mechjacktv.util.DefaultTimeUtils;
import com.mechjacktv.util.ExecutionUtils;
import com.mechjacktv.util.TimeUtils;

public class AbstractCommandTestUtils {

  private final ArbitraryDataGenerator arbitraryDataGenerator;

  public AbstractCommandTestUtils(final ArbitraryDataGenerator arbitraryDataGenerator) {
    this.arbitraryDataGenerator = arbitraryDataGenerator;
  }

  public final CommandUtils givenACommandUtils(final AppConfiguration appConfiguration) {
    final ChatBotConfiguration chatBotConfiguration = this.givenAFakeChatBotConfiguration();
    final ExecutionUtils executionUtils = new DefaultExecutionUtils();
    final TimeUtils timeUtils = new DefaultTimeUtils();

    return new DefaultCommandUtils(appConfiguration, chatBotConfiguration, executionUtils, timeUtils);
  }

  private ChatBotConfiguration givenAFakeChatBotConfiguration() {
    final ChatBotConfiguration chatBotConfiguration = mock(ChatBotConfiguration.class);

    when(chatBotConfiguration.getTwitchChannel()).thenReturn(TwitchChannel.of(this.arbitraryDataGenerator.getString()));
    return chatBotConfiguration;
  }

}