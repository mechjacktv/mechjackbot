package com.mechjacktv.mechjackbot.chatbot;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.pircbotx.User;

import com.mechjacktv.configuration.ConfigurationTestModule;
import com.mechjacktv.mechjackbot.ChatBotConfiguration;
import com.mechjacktv.mechjackbot.command.CommandTestModule;
import com.mechjacktv.testframework.TestFrameworkRule;
import com.mechjacktv.twitchclient.TwitchLogin;
import com.mechjacktv.util.UtilTestModule;

public class PircBotXChatUserUnitTests {

  @Rule
  public final TestFrameworkRule testFrameworkRule = new TestFrameworkRule();

  private void installModules() {
    this.testFrameworkRule.installModule(new ChatBotTestModule());
    this.testFrameworkRule.installModule(new CommandTestModule());
    this.testFrameworkRule.installModule(new ConfigurationTestModule());
    this.testFrameworkRule.installModule(new UtilTestModule());
  }

  private PircBotXChatUser givenIHaveASubjectToTest(final User user) {
    return new PircBotXChatUser(this.testFrameworkRule.getInstance(ChatBotConfiguration.class), user);
  }

  private User givenIHaveAFakeUser() {
    return this.givenIHaveAFakeUser(this.testFrameworkRule.getArbitraryString());
  }

  private User givenIHaveAFakeUser(final String nick) {
    final User user = mock(User.class);

    when(user.getNick()).thenReturn(nick);
    return user;
  }

  @Test
  public final void getTitchLogin_forUser_getNickFromUser() {
    this.installModules();
    final User user = this.givenIHaveAFakeUser();
    final PircBotXChatUser subjectUnderTest = this.givenIHaveASubjectToTest(user);

    subjectUnderTest.getTwitchLogin();

    verify(user).getNick();
  }

  @Test
  public final void getTitchLogin_forUser_wrapsNickValueInTwitchLogin() {
    this.installModules();
    final String nick = this.testFrameworkRule.getArbitraryString();
    final User user = this.givenIHaveAFakeUser(nick);
    final PircBotXChatUser subjectUnderTest = this.givenIHaveASubjectToTest(user);

    final TwitchLogin result = subjectUnderTest.getTwitchLogin();

    assertThat(result).isEqualTo(TwitchLogin.of(nick));
  }

}
