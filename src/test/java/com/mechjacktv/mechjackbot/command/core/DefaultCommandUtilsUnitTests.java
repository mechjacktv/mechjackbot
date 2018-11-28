package com.mechjacktv.mechjackbot.command.core;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.mechjackbot.ChatBotConfiguration;
import com.mechjacktv.mechjackbot.CommandUtils;
import com.mechjacktv.mechjackbot.CommandUtilsContractTests;
import com.mechjacktv.util.DefaultExecutionUtils;
import com.mechjacktv.util.TimeUtils;

public class DefaultCommandUtilsUnitTests extends CommandUtilsContractTests {

  @Override
  protected CommandUtils givenASubjectToTest(final Configuration configuration,
      final ChatBotConfiguration chatBotConfiguration, final TimeUtils timeUtils) {
    return new DefaultCommandUtils(configuration, new DefaultExecutionUtils(), timeUtils);
  }

}
