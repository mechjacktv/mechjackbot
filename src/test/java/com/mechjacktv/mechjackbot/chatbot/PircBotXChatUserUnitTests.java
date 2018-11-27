package com.mechjacktv.mechjackbot.chatbot;

import com.mechjacktv.configuration.MapConfiguration;
import com.mechjacktv.mechjackbot.ChatBotConfiguration;
import com.mechjacktv.mechjackbot.command.DefaultCommandUtils;
import com.mechjacktv.twitchclient.TwitchLogin;
import com.mechjacktv.util.ArbitraryDataGenerator;
import com.mechjacktv.util.DefaultExecutionUtils;
import com.mechjacktv.util.DefaultTimeUtils;
import com.mechjacktv.util.ExecutionUtils;

import org.junit.Test;
import org.pircbotx.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class PircBotXChatUserUnitTests {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();

  private PircBotXChatUser givenIHaveASubjectToTest(final User user) {
    final ChatBotConfiguration chatBotConfiguration = new ArbitraryChatBotConfiguration(this.arbitraryDataGenerator);
    final ExecutionUtils executionUtils = new DefaultExecutionUtils();

    return new PircBotXChatUser(chatBotConfiguration, new DefaultCommandUtils(new MapConfiguration(executionUtils),
        executionUtils, new DefaultTimeUtils()), user);
  }

  private User givenIHaveAFakeUser() {
    return this.givenIHaveAFakeUser(this.arbitraryDataGenerator.getString());
  }

  private User givenIHaveAFakeUser(final String nick) {
    final User user = mock(User.class);

    when(user.getNick()).thenReturn(nick);
    return user;
  }

  @Test
  public final void getTitchLogin_forUser_getNickFromUser() {
    final User user = this.givenIHaveAFakeUser();
    final PircBotXChatUser subjectUnderTest = this.givenIHaveASubjectToTest(user);

    subjectUnderTest.getTwitchLogin();

    verify(user).getNick();
  }

  @Test
  public final void getTitchLogin_forUser_wrapsNickValueInTwitchLogin() {
    final String nick = this.arbitraryDataGenerator.getString();
    final User user = this.givenIHaveAFakeUser(nick);
    final PircBotXChatUser subjectUnderTest = this.givenIHaveASubjectToTest(user);

    final TwitchLogin result = subjectUnderTest.getTwitchLogin();

    assertThat(result).isEqualTo(TwitchLogin.of(nick));
  }

}
