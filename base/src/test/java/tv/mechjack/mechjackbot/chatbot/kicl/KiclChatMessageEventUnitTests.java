package tv.mechjack.mechjackbot.chatbot.kicl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static tv.mechjack.mechjackbot.chatbot.kicl.KiclChatMessageEvent.DEFAULT_RESPONSE_MESSAGE_FORMAT;

import org.junit.Rule;
import org.junit.Test;
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;

import tv.mechjack.mechjackbot.api.ChatBot;
import tv.mechjack.mechjackbot.api.ChatMessage;
import tv.mechjack.mechjackbot.api.ChatUser;
import tv.mechjack.mechjackbot.api.TestCommandModule;
import tv.mechjack.mechjackbot.chatbot.TestChatBotModule;
import tv.mechjack.platform.configuration.Configuration;
import tv.mechjack.platform.configuration.TestConfigurationModule;
import tv.mechjack.platform.utils.ExecutionUtils;
import tv.mechjack.platform.utils.TestUtilsModule;
import tv.mechjack.testframework.TestFramework;

public class KiclChatMessageEventUnitTests {

  @Rule
  public final TestFramework testFrameworkRule = new TestFramework();

  private void installModules() {
    this.testFrameworkRule.registerModule(new TestChatBotModule());
    this.testFrameworkRule.registerModule(new TestCommandModule());
    this.testFrameworkRule.registerModule(new TestConfigurationModule());
    this.testFrameworkRule.registerModule(new TestKiclChatBotModule());
    this.testFrameworkRule.registerModule(new TestUtilsModule());
  }

  private KiclChatMessageEvent givenASubjectToTest() {
    return this.givenASubjectToTest(this.testFrameworkRule.getInstance(ChannelMessageEvent.class));
  }

  private KiclChatMessageEvent givenASubjectToTest(final ChannelMessageEvent channelMessageEvent) {
    return new KiclChatMessageEvent(channelMessageEvent, this.testFrameworkRule.getInstance(ChatBot.class),
        this.testFrameworkRule.getInstance(KiclChatUserFactory.class),
        this.testFrameworkRule.getInstance(Configuration.class),
        this.testFrameworkRule.getInstance(ExecutionUtils.class));
  }

  private ChannelMessageEvent givenAChannelMessageEvent() {
    return this.testFrameworkRule.getInstance(ChannelMessageEvent.class);
  }

  @Test
  public final void getChatBot_whenCalled_returnsNonNullChatBot() {
    this.installModules();
    final KiclChatMessageEvent subjectUnderTest = this.givenASubjectToTest();

    final ChatBot result = subjectUnderTest.getChatBot();

    assertThat(result).isNotNull();
  }

  @Test
  public final void getChatUser_whenCalled_returnsNonNullChatUser() {
    this.installModules();
    final ChannelMessageEvent channelMessageEvent = this.givenAChannelMessageEvent();
    final KiclChatMessageEvent subjectUnderTest = this.givenASubjectToTest(channelMessageEvent);

    final ChatUser result = subjectUnderTest.getChatUser();

    assertThat(result).isNotNull();
  }

  @Test
  public final void getChatUser_whenCalled_wrapsUserFromChannelMessageEvent() {
    this.installModules();
    final ChannelMessageEvent channelMessageEvent = this.givenAChannelMessageEvent();
    final KiclChatMessageEvent subjectUnderTest = this.givenASubjectToTest(channelMessageEvent);

    final ChatUser result = subjectUnderTest.getChatUser();

    assertThat(result.getTwitchLogin().value).isEqualTo(channelMessageEvent.getActor().getNick().toLowerCase());
  }

  @Test
  public final void getMessage_whenCalled_returnsNonNullMessage() {
    this.installModules();
    final ChannelMessageEvent channelMessageEvent = this.givenAChannelMessageEvent();
    final KiclChatMessageEvent subjectUnderTest = this.givenASubjectToTest(channelMessageEvent);

    final ChatMessage result = subjectUnderTest.getChatMessage();

    assertThat(result).isNotNull();
  }

  @Test
  public final void getMessage_whenCalled_wrapsMessageFromChannelMessageEvent() {
    this.installModules();
    final ChannelMessageEvent channelMessageEvent = this.givenAChannelMessageEvent();
    final KiclChatMessageEvent subjectUnderTest = this.givenASubjectToTest(channelMessageEvent);

    final ChatMessage result = subjectUnderTest.getChatMessage();

    assertThat(result.value).isEqualTo(channelMessageEvent.getMessage());
  }

  @Test
  public final void sendResponse_nullMessage_throwsNullPointerException() {
    this.installModules();
    final ChannelMessageEvent channelMessageEvent = this.givenAChannelMessageEvent();
    final KiclChatMessageEvent subjectUnderTest = this.givenASubjectToTest(channelMessageEvent);

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.sendResponse(null));

    this.testFrameworkRule.assertNullPointerException(thrown, "chatMessage");
  }

  @Test
  public final void sendResponse_forMessage_resultIsFormattedMessageSent() {
    this.installModules();
    final TestChannelMessageEvent channelMessageEvent = this.testFrameworkRule
        .getInstance(TestChannelMessageEvent.class);
    final KiclChatMessageEvent subjectUnderTest = this.givenASubjectToTest(channelMessageEvent);

    final ChatMessage chatMessage = ChatMessage.of(this.testFrameworkRule.arbitraryData().getString());
    final String[] result = new String[1];
    channelMessageEvent.setSendReplyHandler(replyMessage -> result[0] = replyMessage);
    subjectUnderTest.sendResponse(chatMessage);

    assertThat(result[0]).isEqualTo(String.format(DEFAULT_RESPONSE_MESSAGE_FORMAT, chatMessage.value));
  }

}
