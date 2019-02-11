package tv.mechjack.mechjackbot.chatbot.kicl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.element.Channel;
import org.kitteh.irc.client.library.element.MessageTag;
import org.kitteh.irc.client.library.element.ServerMessage;
import org.kitteh.irc.client.library.element.User;
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import tv.mechjack.mechjackbot.api.TestCommandModule;
import tv.mechjack.mechjackbot.api.UserRole;
import tv.mechjack.mechjackbot.chatbot.TestChatBotModule;
import tv.mechjack.platform.configuration.TestConfigurationModule;
import tv.mechjack.platform.utils.TestUtilsModule;
import tv.mechjack.testframework.TestFramework;
import tv.mechjack.testframework.fake.FakeBuilder;
import tv.mechjack.twitchclient.TwitchLogin;

@RunWith(JUnitParamsRunner.class)
public class KiclChatUserUnitTests {

  private static final String BROADCASTER_BADGE = "broadcaster/3,subscriber/12,turbo/1";
  private static final String MODERATOR_BADGE = "moderator/1,bits-charity/1";
  private static final String MODERATOR_SUBSCRIBER_BADGES = "moderator/1,subscriber/3,bits-charity/1";
  private static final String NON_ROLE_BADGE = "turbo/1";
  private static final String SUBSCRIBER_BADGE = "subscriber/3,turbo/1";
  private static final String VIP_BADGE = "vip/1,turbo/1";
  private static final String VIP_SUBSCRIBER_BADGE = "vip/1,subscriber/0,bits-charity/1";

  @Rule
  public final TestFramework testFrameworkRule = new TestFramework();

  private void installModules() {
    this.testFrameworkRule.registerModule(new TestChatBotModule());
    this.testFrameworkRule.registerModule(new TestCommandModule());
    this.testFrameworkRule.registerModule(new TestConfigurationModule());
    this.testFrameworkRule.registerModule(new TestKiclChatBotModule());
    this.testFrameworkRule.registerModule(new TestUtilsModule());
  }

  private KiclChatUser givenIHaveASubjectToTest(final ChannelMessageEvent event) {
    return new KiclChatUser(event);
  }

  private ChannelMessageEvent givenAFakeChannelMessageEvent(final User user) {
    return this.givenAFakeChannelMessageEvent(user, this.givenAFakeServerMessage(null));
  }

  private ChannelMessageEvent givenAFakeChannelMessageEvent(final ServerMessage serverMessage) {
    return this.givenAFakeChannelMessageEvent(this.testFrameworkRule.getInstance(User.class), serverMessage);
  }

  private ChannelMessageEvent givenAFakeChannelMessageEvent(final User user, final ServerMessage serverMessage) {
    return new ChannelMessageEvent(this.testFrameworkRule.getInstance(Client.class),
        Lists.newArrayList(serverMessage), user, this.testFrameworkRule.getInstance(Channel.class),
        this.testFrameworkRule.arbitraryData().getString());
  }

  private ServerMessage givenAFakeServerMessage(final String badgesTagValue) {
    final FakeBuilder<MessageTag> messageTagFakeBuilder = this.testFrameworkRule.fakeBuilder(MessageTag.class);

    messageTagFakeBuilder.forMethod("getName").addHandler(invocation -> "badges");
    messageTagFakeBuilder.forMethod("getValue").addHandler(invocation -> Optional.ofNullable(badgesTagValue));

    final MessageTag messageTag = messageTagFakeBuilder.build();
    final FakeBuilder<ServerMessage> serverMessageFakeBuilder = this.testFrameworkRule.fakeBuilder(ServerMessage.class);

    serverMessageFakeBuilder.forMethod("getTag", new Class[] { String.class }).addHandler(invocation -> {
      if ("badges".equals(invocation.getArgument(0))) {
        return Optional.of(messageTag);
      }
      return null;
    });
    return serverMessageFakeBuilder.build();
  }

  @Test
  public final void getTwitchLogin_forNick_resultIsTwitchLoginWithNick() {
    this.installModules();
    final User user = this.testFrameworkRule.getInstance(User.class);
    final ChannelMessageEvent channelMessageEvent = this.givenAFakeChannelMessageEvent(user);
    final KiclChatUser subjectUnderTest = this.givenIHaveASubjectToTest(channelMessageEvent);

    final TwitchLogin result = subjectUnderTest.getTwitchLogin();

    assertThat(result).isEqualTo(TwitchLogin.of(user.getNick()));
  }

  @Test
  @Parameters(method = "badgesToRole")
  public final void getUserRole_forBadges_resultIsExpected(final String badges, final UserRole expected) {
    this.installModules();
    final ServerMessage serverMessage = this.givenAFakeServerMessage(badges);
    final ChannelMessageEvent channelMessageEvent = this.givenAFakeChannelMessageEvent(serverMessage);
    final KiclChatUser subjectUnderTest = this.givenIHaveASubjectToTest(channelMessageEvent);

    final UserRole result = subjectUnderTest.getUserRole();

    assertThat(result).isEqualTo(expected);
  }

  @Test
  @Parameters(method = "badgesHasRole")
  public final void hasUserRole_forBadgesAndUserRole_resultIsExpected(final String badges,
      final UserRole userRole, boolean expected) {
    this.installModules();
    final ServerMessage serverMessage = this.givenAFakeServerMessage(badges);
    final ChannelMessageEvent channelMessageEvent = this.givenAFakeChannelMessageEvent(serverMessage);
    final KiclChatUser subjectUnderTest = this.givenIHaveASubjectToTest(channelMessageEvent);

    final boolean result = subjectUnderTest.hasAccessLevel(userRole);

    assertThat(result).isEqualTo(expected);
  }

  public final Object badgesToRole() {
    return new Object[] {
        new Object[] { BROADCASTER_BADGE, UserRole.BROADCASTER },

        new Object[] { MODERATOR_BADGE, UserRole.MODERATOR },
        new Object[] { MODERATOR_SUBSCRIBER_BADGES, UserRole.MODERATOR },

        new Object[] { VIP_BADGE, UserRole.VIP },
        new Object[] { VIP_SUBSCRIBER_BADGE, UserRole.VIP },

        new Object[] { SUBSCRIBER_BADGE, UserRole.SUBSCRIBER },

        new Object[] { NON_ROLE_BADGE, UserRole.VIEWER },

        new Object[] { null, UserRole.VIEWER }
    };
  }

  public final Object badgesHasRole() {
    return new Object[] {
        new Object[] { BROADCASTER_BADGE, UserRole.BROADCASTER, true },
        new Object[] { BROADCASTER_BADGE, UserRole.MODERATOR, true },
        new Object[] { BROADCASTER_BADGE, UserRole.VIP, true },
        new Object[] { BROADCASTER_BADGE, UserRole.SUBSCRIBER, true },
        new Object[] { BROADCASTER_BADGE, UserRole.VIEWER, true },

        new Object[] { MODERATOR_BADGE, UserRole.BROADCASTER, false },
        new Object[] { MODERATOR_BADGE, UserRole.MODERATOR, true },
        new Object[] { MODERATOR_BADGE, UserRole.VIP, true },
        new Object[] { MODERATOR_BADGE, UserRole.SUBSCRIBER, true },
        new Object[] { MODERATOR_BADGE, UserRole.VIEWER, true },

        new Object[] { VIP_BADGE, UserRole.BROADCASTER, false },
        new Object[] { VIP_BADGE, UserRole.MODERATOR, false },
        new Object[] { VIP_BADGE, UserRole.VIP, true },
        new Object[] { VIP_BADGE, UserRole.SUBSCRIBER, true },
        new Object[] { VIP_BADGE, UserRole.VIEWER, true },

        new Object[] { SUBSCRIBER_BADGE, UserRole.BROADCASTER, false },
        new Object[] { SUBSCRIBER_BADGE, UserRole.MODERATOR, false },
        new Object[] { SUBSCRIBER_BADGE, UserRole.VIP, false },
        new Object[] { SUBSCRIBER_BADGE, UserRole.SUBSCRIBER, true },
        new Object[] { SUBSCRIBER_BADGE, UserRole.VIEWER, true },

        new Object[] { NON_ROLE_BADGE, UserRole.BROADCASTER, false },
        new Object[] { NON_ROLE_BADGE, UserRole.MODERATOR, false },
        new Object[] { NON_ROLE_BADGE, UserRole.VIP, false },
        new Object[] { NON_ROLE_BADGE, UserRole.SUBSCRIBER, false },
        new Object[] { NON_ROLE_BADGE, UserRole.VIEWER, true },

        new Object[] { null, UserRole.BROADCASTER, false },
        new Object[] { null, UserRole.MODERATOR, false },
        new Object[] { null, UserRole.VIP, false },
        new Object[] { null, UserRole.SUBSCRIBER, false },
        new Object[] { null, UserRole.VIEWER, true }
    };
  }

}
