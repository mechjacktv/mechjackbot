package tv.mechjack.mechjackbot.feature.wouldyourather;

import static org.assertj.core.api.Assertions.assertThat;
import static tv.mechjack.mechjackbot.api.BaseChatCommand.KEY_DESCRIPTION;
import static tv.mechjack.mechjackbot.api.BaseChatCommand.KEY_TRIGGER;

import java.io.StringReader;

import org.junit.Test;

import tv.mechjack.mechjackbot.api.BaseChatCommandContractTests;
import tv.mechjack.mechjackbot.api.ChatCommandDescription;
import tv.mechjack.mechjackbot.api.ChatCommandTrigger;
import tv.mechjack.mechjackbot.api.ChatMessage;
import tv.mechjack.mechjackbot.api.CommandConfigurationBuilder;
import tv.mechjack.mechjackbot.api.TestChatMessageEvent;
import tv.mechjack.mechjackbot.api.TestCommandModule;
import tv.mechjack.platform.TestPlatformModule;
import tv.mechjack.platform.configuration.ConfigurationKey;
import tv.mechjack.platform.utils.RandomUtils;
import tv.mechjack.testframework.fake.FakeBuilder;

public class WouldYouRatherChatCommandUnitTests extends BaseChatCommandContractTests {

  @Override
  protected void installModules() {
    this.testFrameworkRule.registerModule(new TestCommandModule());
    this.testFrameworkRule.registerModule(new TestPlatformModule());
  }

  @Override
  protected WouldYouRatherChatCommand givenASubjectToTest() {
    final FakeBuilder<QuestionsDataSource> fakeBuilder = this.testFrameworkRule.fakeBuilder(QuestionsDataSource.class);
    fakeBuilder.forMethod("getQuestions").setHandler(invocation -> new StringReader(""));
    return this.givenASubjectToTest(fakeBuilder.build());
  }

  protected WouldYouRatherChatCommand givenASubjectToTest(final QuestionsDataSource questionsDataSource) {
    return new WouldYouRatherChatCommand(this.testFrameworkRule.getInstance(CommandConfigurationBuilder.class),
        this.testFrameworkRule.getInstance(RandomUtils.class), questionsDataSource);
  }

  @Override
  protected ConfigurationKey getDescriptionKey() {
    return ConfigurationKey.of(KEY_DESCRIPTION, WouldYouRatherChatCommand.class);
  }

  @Override
  protected ChatCommandDescription getDescriptionDefault() {
    return ChatCommandDescription.of(WouldYouRatherChatCommand.DEFAULT_DESCRIPTION);
  }

  @Override
  protected ConfigurationKey getTriggerKey() {
    return ConfigurationKey.of(KEY_TRIGGER, WouldYouRatherChatCommand.class);
  }

  @Override
  protected ChatCommandTrigger getTriggerDefault() {
    return ChatCommandTrigger.of(WouldYouRatherChatCommand.DEFAULT_TRIGGER);
  }

  @Test
  public final void handleMessageEvent_noQuestions_resultIsNoMessageSent() {
    this.installModules();
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final WouldYouRatherChatCommand subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    assertThat(result).isNull();
  }

  @Test
  public final void handleMessageEvent_withQuestions_resultIsIndicatedRandomQuestion() {
    this.installModules();
    final String desiredQuestion = this.testFrameworkRule.arbitraryData().getString();
    final TestQuestionsDataSource questionsDataSource = new TestQuestionsDataSource();
    questionsDataSource.addQuestion(this.testFrameworkRule.arbitraryData().getString());
    questionsDataSource.addQuestion(desiredQuestion);
    questionsDataSource.addQuestion(this.testFrameworkRule.arbitraryData().getString());
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    this.testFrameworkRule.testRandom().setNextValues(1);
    final WouldYouRatherChatCommand subjectUnderTest = this.givenASubjectToTest(questionsDataSource);

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    assertThat(result).isEqualTo(
        ChatMessage.of(String.format(WouldYouRatherChatCommand.DEFAULT_MESSAGE_FORMAT, desiredQuestion)));
  }

  @Test
  public final void handleMessageEvent_withCommentLine_resultIsExpectedQuestion() {
    this.installModules();
    final String desiredQuestion = this.testFrameworkRule.arbitraryData().getString();
    final TestQuestionsDataSource questionsDataSource = new TestQuestionsDataSource();
    questionsDataSource.addQuestion("#" + this.testFrameworkRule.arbitraryData().getString());
    questionsDataSource.addQuestion(desiredQuestion);
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    this.testFrameworkRule.testRandom().setNextValues(0);
    final WouldYouRatherChatCommand subjectUnderTest = this.givenASubjectToTest(questionsDataSource);

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    assertThat(result).isEqualTo(
        ChatMessage.of(String.format(WouldYouRatherChatCommand.DEFAULT_MESSAGE_FORMAT, desiredQuestion)));
  }

  @Test
  public final void handleMessageEvent_withTrailingQuestionMark_resultIsExpectedQuestion() {
    this.installModules();
    final String desiredQuestion = this.testFrameworkRule.arbitraryData().getString();
    final TestQuestionsDataSource questionsDataSource = new TestQuestionsDataSource();
    questionsDataSource.addQuestion(desiredQuestion + "?");
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    this.testFrameworkRule.testRandom().setNextValues(0);
    final WouldYouRatherChatCommand subjectUnderTest = this.givenASubjectToTest(questionsDataSource);

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    assertThat(result).isEqualTo(
        ChatMessage.of(String.format(WouldYouRatherChatCommand.DEFAULT_MESSAGE_FORMAT, desiredQuestion)));
  }

  @Test
  public final void handleMessageEvent_withPrecedingWouldYouRather_resultIsExpectedQuestion() {
    this.installModules();
    final String desiredQuestion = this.testFrameworkRule.arbitraryData().getString();
    final TestQuestionsDataSource questionsDataSource = new TestQuestionsDataSource();
    questionsDataSource.addQuestion("Would you rather " + desiredQuestion);
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    this.testFrameworkRule.testRandom().setNextValues(0);
    final WouldYouRatherChatCommand subjectUnderTest = this.givenASubjectToTest(questionsDataSource);

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    assertThat(result).isEqualTo(
        ChatMessage.of(String.format(WouldYouRatherChatCommand.DEFAULT_MESSAGE_FORMAT, desiredQuestion)));
  }

}
