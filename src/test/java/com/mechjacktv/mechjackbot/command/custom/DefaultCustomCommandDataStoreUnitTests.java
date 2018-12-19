package com.mechjacktv.mechjackbot.command.custom;

import java.util.HashMap;
import java.util.Map;

import com.mechjacktv.keyvaluestore.MapKeyValueStore;
import com.mechjacktv.keyvaluestore.MessageStoreContractTests;
import com.mechjacktv.mechjackbot.AccessLevel;
import com.mechjacktv.mechjackbot.CommandTrigger;
import com.mechjacktv.proto.mechjackbot.command.custom.CustomComandDataStoreMessage.CustomCommand;
import com.mechjacktv.proto.mechjackbot.command.custom.CustomComandDataStoreMessage.CustomCommandKey;
import com.mechjacktv.util.ExecutionUtils;
import com.mechjacktv.util.ProtobufUtils;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class DefaultCustomCommandDataStoreUnitTests extends MessageStoreContractTests<CustomCommandKey, CustomCommand> {

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
        .setTrigger(this.testFrameworkRule.getArbitraryString())
        .build();
  }

  @Override
  protected CustomCommand givenAValue() {
    return CustomCommand.newBuilder()
        .setTrigger(this.testFrameworkRule.getArbitraryString())
        .setCommandBody(this.testFrameworkRule.getArbitraryString())
        .setAccessLevel(AccessLevel.FOLLOWER.toString())
        .build();
  }

  @Test
  public final void createCustomCommandKey_nullTrigger_throwsNullPointerException() {
    this.installModules();
    final DefaultCustomCommandDataStore subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.createCustomCommandKey(null));

    this.testFrameworkRule.assertNullPointerException(thrown, "trigger");
  }

  @Test
  public final void createCustomCommandKey_forTrigger_resultIsCustomCommandKeyWithTrigger() {
    this.installModules();
    final String trigger = this.testFrameworkRule.getArbitraryString();
    final DefaultCustomCommandDataStore subjectUnderTest = this.givenASubjectToTest();

    final CustomCommandKey result = subjectUnderTest.createCustomCommandKey(CommandTrigger.of(trigger));

    assertThat(result.getTrigger()).isEqualTo(trigger);
  }

  @Test
  public final void createCustomCommand_nullTrigger_throwsNullPointerException() {
    this.installModules();
    final DefaultCustomCommandDataStore subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.createCustomCommand(null,
        CommandBody.of(this.testFrameworkRule.getArbitraryString()), AccessLevel.FOLLOWER));

    this.testFrameworkRule.assertNullPointerException(thrown, "trigger");
  }

  @Test
  public final void createCustomCommand_nullCommandBody_throwsNullPointerException() {
    this.installModules();
    final DefaultCustomCommandDataStore subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.createCustomCommand(
        CommandTrigger.of(this.testFrameworkRule.getArbitraryString()), null, AccessLevel.FOLLOWER));

    this.testFrameworkRule.assertNullPointerException(thrown, "commandBody");
  }

  @Test
  public final void createCustomCommand_nullAccessLevel_throwsNullPointerException() {
    this.installModules();
    final DefaultCustomCommandDataStore subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.createCustomCommand(
        CommandTrigger.of(this.testFrameworkRule.getArbitraryString()),
        CommandBody.of(this.testFrameworkRule.getArbitraryString()), null));

    this.testFrameworkRule.assertNullPointerException(thrown, "accessLevel");
  }

  @Test
  public final void createCustomCommand_forTriggerBodyAndAccessLevel_resultIsCustomCommandKeyWithTriggerBodyAndAccessLevel() {
    this.installModules();
    final String trigger = this.testFrameworkRule.getArbitraryString();
    final String commandBody = this.testFrameworkRule.getArbitraryString();
    final DefaultCustomCommandDataStore subjectUnderTest = this.givenASubjectToTest();

    final CustomCommand result = subjectUnderTest.createCustomCommand(CommandTrigger.of(trigger),
        CommandBody.of(commandBody), AccessLevel.FOLLOWER);

    final SoftAssertions softly = new SoftAssertions();
    softly.assertThat(result.getTrigger()).isEqualTo(trigger);
    softly.assertThat(result.getCommandBody()).isEqualTo(commandBody);
    softly.assertThat(result.getAccessLevel()).isEqualTo(AccessLevel.FOLLOWER.toString());
    softly.assertAll();
  }

}
