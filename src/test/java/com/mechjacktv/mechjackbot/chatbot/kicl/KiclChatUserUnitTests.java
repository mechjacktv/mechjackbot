package com.mechjacktv.mechjackbot.chatbot.kicl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.element.User;

import com.mechjacktv.configuration.ConfigurationTestModule;
import com.mechjacktv.mechjackbot.ChatBotConfiguration;
import com.mechjacktv.mechjackbot.chatbot.ChatBotTestModule;
import com.mechjacktv.mechjackbot.command.CommandTestModule;
import com.mechjacktv.testframework.TestFrameworkRule;
import com.mechjacktv.twitchclient.TwitchLogin;
import com.mechjacktv.util.UtilTestModule;

public class KiclChatUserUnitTests {

  @Rule
  public final TestFrameworkRule testFrameworkRule = new TestFrameworkRule();

  private void installModules() {
    this.testFrameworkRule.installModule(new ChatBotTestModule());
    this.testFrameworkRule.installModule(new CommandTestModule());
    this.testFrameworkRule.installModule(new ConfigurationTestModule());
    this.testFrameworkRule.installModule(new KiclChatBotTestModule());
    this.testFrameworkRule.installModule(new UtilTestModule());
  }

  private KiclChatUser givenIHaveASubjectToTest(final User user) {
    return new KiclChatUser(this.testFrameworkRule.getInstance(ChatBotConfiguration.class), user);
  }

  private User givenIHaveAFakeUser() {
    return this.givenIHaveAFakeUser(this.testFrameworkRule.getArbitraryString());
  }

  private User givenIHaveAFakeUser(final String nick) {
    final User user = mock(User.class);

    when(user.getClient()).thenReturn(this.testFrameworkRule.getInstance(Client.class));
    when(user.getNick()).thenReturn(nick);
    return user;
  }

  @Test
  public final void getTitchLogin_forUser_getNickFromUser() {
    this.installModules();
    final User user = this.givenIHaveAFakeUser();
    final KiclChatUser subjectUnderTest = this.givenIHaveASubjectToTest(user);

    subjectUnderTest.getTwitchLogin();

    verify(user).getNick();
  }

  @Test
  public final void getTitchLogin_forUser_wrapsNickValueInTwitchLogin() {
    this.installModules();
    final String nick = this.testFrameworkRule.getArbitraryString();
    final User user = this.givenIHaveAFakeUser(nick);
    final KiclChatUser subjectUnderTest = this.givenIHaveASubjectToTest(user);

    final TwitchLogin result = subjectUnderTest.getTwitchLogin();

    assertThat(result).isEqualTo(TwitchLogin.of(nick));
  }

  // TODO (2018-12-22 mechjack): test hasUserRole once really implemented

}
