package tv.mechjack.mechjackbot.feature.custom;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.HashMap;
import java.util.Map;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import tv.mechjack.mechjackbot.api.ChatCommandDescription;
import tv.mechjack.mechjackbot.api.ChatCommandTrigger;
import tv.mechjack.mechjackbot.api.UserRole;
import tv.mechjack.mechjackbot.feature.custom.ProtoMessage.CustomCommand;
import tv.mechjack.mechjackbot.feature.custom.ProtoMessage.CustomCommandKey;
import tv.mechjack.platform.keyvaluestore.ChatMessageStoreContractTests;
import tv.mechjack.platform.keyvaluestore.MapKeyValueStore;
import tv.mechjack.platform.utils.ExecutionUtils;
import tv.mechjack.platform.utils.ProtobufUtils;

public class DefaultCustomCommandDataStoreUnitTests extends
    ChatMessageStoreContractTests<CustomCommandKey, CustomCommand> {

  @Test
  public final void createCustomCommandKey_nullTrigger_throwsNullPointerException() {
    this.installModules();
    final DefaultCustomCommandDataStore subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.createCustomCommandKey(null));

    assertThat(thrown).isInstanceOf(NullPointerException.class)
        .hasMessage(this.testFrameworkRule.getInstance(ExecutionUtils.class)
            .nullMessageForName("trigger"));
  }

  @Override
  protected DefaultCustomCommandDataStore givenASubjectToTest() {
    return this.givenASubjectToTest(new HashMap<>());
  }

  @Override
  protected DefaultCustomCommandDataStore givenASubjectToTest(
      final Map<CustomCommandKey, CustomCommand> data) {
    final MapKeyValueStore dataStore = new MapKeyValueStore();

    for (final CustomCommandKey key : data.keySet()) {
      dataStore.put(key.toByteArray(), data.get(key).toByteArray());
    }

    return new DefaultCustomCommandDataStore((name) -> dataStore,
        this.testFrameworkRule.getInstance(ExecutionUtils.class),
        this.testFrameworkRule.getInstance(ProtobufUtils.class));
  }

  @Override
  protected CustomCommandKey givenAKey() {
    return CustomCommandKey.newBuilder()
        .setTrigger(this.testFrameworkRule.arbitraryData().getString())
        .build();
  }

  @Override
  protected CustomCommand givenAValue() {
    return CustomCommand.newBuilder()
        .setTrigger(this.testFrameworkRule.arbitraryData().getString())
        .setCommandBody(this.testFrameworkRule.arbitraryData().getString())
        .setAccessLevel(UserRole.SUBSCRIBER.toString())
        .build();
  }

  @Test
  public final void createCustomCommandKey_forTrigger_resultIsCustomCommandKeyWithTrigger() {
    this.installModules();
    final String trigger = this.testFrameworkRule.arbitraryData().getString();
    final DefaultCustomCommandDataStore subjectUnderTest = this.givenASubjectToTest();

    final CustomCommandKey result = subjectUnderTest.createCustomCommandKey(ChatCommandTrigger.of(trigger));

    assertThat(result.getTrigger()).isEqualTo(trigger);
  }

  @Test
  public final void createCustomCommand_nullTrigger_throwsNullPointerException() {
    this.installModules();
    final DefaultCustomCommandDataStore subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.createCustomCommand(null,
        CommandBody.of(this.testFrameworkRule.arbitraryData().getString()),
        ChatCommandDescription.of(this.testFrameworkRule.arbitraryData().getString()), UserRole.SUBSCRIBER));

    assertThat(thrown).isInstanceOf(NullPointerException.class)
        .hasMessage(this.testFrameworkRule.getInstance(ExecutionUtils.class)
            .nullMessageForName("trigger"));
  }

  @Test
  public final void createCustomCommand_nullCommandBody_throwsNullPointerException() {
    this.installModules();
    final DefaultCustomCommandDataStore subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.createCustomCommand(
        ChatCommandTrigger.of(this.testFrameworkRule.arbitraryData().getString()), null,
        ChatCommandDescription.of(this.testFrameworkRule.arbitraryData().getString()), UserRole.SUBSCRIBER));

    assertThat(thrown).isInstanceOf(NullPointerException.class)
        .hasMessage(this.testFrameworkRule.getInstance(ExecutionUtils.class)
            .nullMessageForName("commandBody"));
  }

  @Test
  public final void createCustomCommand_nullDescription_throwsNullPointerException() {
    this.installModules();
    final DefaultCustomCommandDataStore subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.createCustomCommand(
        ChatCommandTrigger.of(this.testFrameworkRule.arbitraryData().getString()),
        CommandBody.of(this.testFrameworkRule.arbitraryData().getString()),
        null, UserRole.SUBSCRIBER));

    assertThat(thrown).isInstanceOf(NullPointerException.class)
        .hasMessage(this.testFrameworkRule.getInstance(ExecutionUtils.class)
            .nullMessageForName("description"));
  }

  @Test
  public final void createCustomCommand_nullAccessLevel_throwsNullPointerException() {
    this.installModules();
    final DefaultCustomCommandDataStore subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.createCustomCommand(
        ChatCommandTrigger.of(this.testFrameworkRule.arbitraryData().getString()),
        CommandBody.of(this.testFrameworkRule.arbitraryData().getString()),
        ChatCommandDescription.of(this.testFrameworkRule.arbitraryData().getString()), null));

    assertThat(thrown).isInstanceOf(NullPointerException.class)
        .hasMessage(this.testFrameworkRule.getInstance(ExecutionUtils.class)
            .nullMessageForName("userRole"));
  }

  @Test
  public final void createCustomCommand_forTriggerBodyAndAccessLevel_resultIsCustomCommandKeyWithTriggerBodyAndAccessLevel() {
    this.installModules();
    final String trigger = this.testFrameworkRule.arbitraryData().getString();
    final String commandBody = this.testFrameworkRule.arbitraryData().getString();
    final String description = this.testFrameworkRule.arbitraryData().getString();
    final DefaultCustomCommandDataStore subjectUnderTest = this.givenASubjectToTest();

    final CustomCommand result = subjectUnderTest.createCustomCommand(ChatCommandTrigger.of(trigger),
        CommandBody.of(commandBody), ChatCommandDescription.of(description), UserRole.SUBSCRIBER);

    final SoftAssertions softly = new SoftAssertions();
    softly.assertThat(result.getTrigger()).isEqualTo(trigger);
    softly.assertThat(result.getCommandBody()).isEqualTo(commandBody);
    softly.assertThat(result.getDescription()).isEqualTo(description);
    softly.assertThat(result.getAccessLevel()).isEqualTo(UserRole.SUBSCRIBER.toString());
    softly.assertAll();
  }

}
