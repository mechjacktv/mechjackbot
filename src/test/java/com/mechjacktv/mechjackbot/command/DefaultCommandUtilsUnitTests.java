package com.mechjacktv.mechjackbot.command;

import com.mechjacktv.mechjackbot.AppConfiguration;
import com.mechjacktv.mechjackbot.ChatBotConfiguration;
import com.mechjacktv.mechjackbot.CommandUtils;
import com.mechjacktv.mechjackbot.CommandUtilsContractTests;
import com.mechjacktv.util.DefaultExecutionUtils;
import com.mechjacktv.util.TimeUtils;

public class DefaultCommandUtilsUnitTests extends CommandUtilsContractTests {

  @Override
  protected CommandUtils givenASubjectToTest(final AppConfiguration appConfiguration,
      final ChatBotConfiguration chatBotConfiguration, final TimeUtils timeUtils) {
    return new DefaultCommandUtils(appConfiguration, new DefaultExecutionUtils(), timeUtils);
  }

}
