package com.mechjacktv.mechjackbot.chatbot;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.pircbotx.User;

import com.mechjacktv.mechjackbot.ChatUsername;
import com.mechjacktv.util.ArbitraryDataGenerator;

public class PircBotXChatUserUnitTests {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();

  private PircBotXChatUser givenIHaveASubjectToTest(final User user) {
    return new PircBotXChatUser(user);
  }

  private User givenIHaveAUser() {
    return this.givenIHaveAUser(this.arbitraryDataGenerator.getString());
  }

  private User givenIHaveAUser(final String nick) {
    final User user = mock(User.class);

    when(user.getNick()).thenReturn(nick);
    return user;
  }

  @Test
  public final void getUsername_forUser_getNickFromUser() {
    final User user = this.givenIHaveAUser();
    final PircBotXChatUser subjectUnderTest = this.givenIHaveASubjectToTest(user);

    subjectUnderTest.getUsername();

    verify(user).getNick();
  }

  @Test
  public final void getUsername_forUser_wrapsNickValueInChatUsername() {
    final String nick = this.arbitraryDataGenerator.getString();
    final User user = this.givenIHaveAUser(nick);
    final PircBotXChatUser subjectUnderTest = this.givenIHaveASubjectToTest(user);

    final ChatUsername result = subjectUnderTest.getUsername();

    assertThat(result.value).isEqualTo(nick);
  }

}
