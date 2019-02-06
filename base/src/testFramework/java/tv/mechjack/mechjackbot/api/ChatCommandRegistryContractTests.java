package tv.mechjack.mechjackbot.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.Collection;
import java.util.Optional;

import org.assertj.core.api.Condition;
import org.junit.Rule;
import org.junit.Test;

import tv.mechjack.platform.configuration.TestConfigurationModule;
import tv.mechjack.platform.utils.TestUtilsModule;
import tv.mechjack.testframework.TestFrameworkRule;

public abstract class ChatCommandRegistryContractTests {

  @Rule
  public final TestFrameworkRule testFrameworkRule = new TestFrameworkRule();

  protected void installModules() {
    this.testFrameworkRule.installModule(new TestCommandModule());
    this.testFrameworkRule.installModule(new TestConfigurationModule());
    this.testFrameworkRule.installModule(new TestUtilsModule());
  }

  protected abstract ChatCommandRegistry givenASubjectToTest();

  @Test
  public final void addCommand_nullCommand_throwsNullPointerException() {
    this.installModules();
    final ChatCommandRegistry subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.addCommand(null));

    this.testFrameworkRule.assertNullPointerException(thrown, "chatCommand");
  }

  @Test
  public final void addCommandAndGetCommands_forCommand_storesCommand() {
    this.installModules();
    final ChatCommand chatCommand = this.testFrameworkRule.getInstance(TestChatCommand.class);
    final ChatCommandRegistry subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.addCommand(chatCommand);
    final Collection<ChatCommand> result = subjectUnderTest.getCommands();

    assertThat(result).containsOnly(chatCommand);
  }

  @Test
  public final void addCommandAndGetCommand_forCommand_mapsCommandToTrigger() {
    this.installModules();
    final ChatCommand chatCommand = this.testFrameworkRule.getInstance(TestChatCommand.class);
    final ChatCommandRegistry subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.addCommand(chatCommand);
    final Optional<ChatCommand> result = subjectUnderTest.getCommand(chatCommand.getTrigger());

    assertThat(result).is(new Condition<Optional<ChatCommand>>() {

      @Override
      public boolean matches(final Optional<ChatCommand> value) {
        return value.isPresent() && value.get().equals(chatCommand);
      }

    });
  }

  @Test
  public final void hasCommand_nullTrigger_throwsNullPointerException() {
    this.installModules();
    final ChatCommandRegistry subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.hasCommand(null));

    this.testFrameworkRule.assertNullPointerException(thrown, "trigger");
  }

  @Test
  public final void hasCommand_noCommandRegistered_resultIsFalse() {
    this.installModules();
    final ChatCommandRegistry subjectUnderTest = this.givenASubjectToTest();

    final boolean result = subjectUnderTest
        .hasCommand(ChatCommandTrigger.of(this.testFrameworkRule.getArbitraryString()));

    assertThat(result).isFalse();
  }

  @Test
  public final void hasCommand_commandRegistered_resultIsTrue() {
    this.installModules();
    final ChatCommand command = this.testFrameworkRule.getInstance(TestChatCommand.class);
    final ChatCommandRegistry subjectUnderTest = this.givenASubjectToTest();
    subjectUnderTest.addCommand(command);

    final boolean result = subjectUnderTest.hasCommand(command.getTrigger());

    assertThat(result).isTrue();
  }

  @Test
  public final void removeCommand_nullTrigger_throwsNullPointerException() {
    this.installModules();
    final ChatCommandRegistry subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.removeCommand(null));

    this.testFrameworkRule.assertNullPointerException(thrown, "trigger");
  }

  @Test
  public final void removeCommand_noCommandRegistered_resultIsFalse() {
    this.installModules();
    final ChatCommandRegistry subjectUnderTest = this.givenASubjectToTest();

    final boolean result = subjectUnderTest
        .removeCommand(ChatCommandTrigger.of(this.testFrameworkRule.getArbitraryString()));

    assertThat(result).isFalse();
  }

  @Test
  public final void removeCommand_commandRegistered_resultIsTrue() {
    this.installModules();
    final ChatCommand command = this.testFrameworkRule.getInstance(TestChatCommand.class);
    final ChatCommandRegistry subjectUnderTest = this.givenASubjectToTest();
    subjectUnderTest.addCommand(command);

    final boolean result = subjectUnderTest.removeCommand(command.getTrigger());

    assertThat(result).isTrue();
  }

}
