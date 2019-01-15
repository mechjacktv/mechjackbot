package tv.mechjack.mechjackbot.chatbot.kicl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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
import tv.mechjack.platform.util.ExecutionUtils;
import tv.mechjack.platform.util.TestUtilModule;
import tv.mechjack.testframework.TestFrameworkRule;

public class KiclChatMessageEventUnitTests {

  @Rule
  public final TestFrameworkRule testFrameworkRule = new TestFrameworkRule();

  private void installModules() {
    this.testFrameworkRule.installModule(new TestChatBotModule());
    this.testFrameworkRule.installModule(new TestCommandModule());
    this.testFrameworkRule.installModule(new TestConfigurationModule());
    this.testFrameworkRule.installModule(new TestKiclChatBotModule());
    this.testFrameworkRule.installModule(new TestUtilModule());
  }

  private KiclChatMessageEvent givenASubjectToTest() {
    return this.givenASubjectToTest(mock(ChannelMessageEvent.class));
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
    final ChatMessage chatMessage = ChatMessage.of(this.testFrameworkRule.getArbitraryString());
    final ChannelMessageEvent channelMessageEvent = this.givenAChannelMessageEvent();
    final KiclChatMessageEvent subjectUnderTest = this.givenASubjectToTest(channelMessageEvent);

    subjectUnderTest.sendResponse(chatMessage);

    verify(channelMessageEvent.getChannel()).sendMessage(String.format(DEFAULT_RESPONSE_MESSAGE_FORMAT, chatMessage));
  }

}
