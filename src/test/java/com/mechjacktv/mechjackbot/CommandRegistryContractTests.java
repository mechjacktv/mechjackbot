package com.mechjacktv.mechjackbot;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.Collection;
import java.util.Optional;

import org.assertj.core.api.Condition;
import org.junit.Rule;
import org.junit.Test;

import com.mechjacktv.configuration.ConfigurationTestModule;
import com.mechjacktv.mechjackbot.command.CommandTestModule;
import com.mechjacktv.testframework.TestFrameworkRule;
import com.mechjacktv.util.UtilTestModule;

public abstract class CommandRegistryContractTests {

  @Rule
  public final TestFrameworkRule testFrameworkRule = new TestFrameworkRule();

  protected void installModules() {
    this.testFrameworkRule.installModule(new CommandTestModule());
    this.testFrameworkRule.installModule(new ConfigurationTestModule());
    this.testFrameworkRule.installModule(new UtilTestModule());
  }

  protected abstract CommandRegistry givenASubjectToTest();

  @Test
  public final void addCommand_nullCommand_throwsNullPointerException() {
    this.installModules();
    final CommandRegistry subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.addCommand(null));

    this.testFrameworkRule.assertNullPointerException(thrown, "command");
  }

  @Test
  public final void addCommandAndGetCommands_forCommand_storesCommand() {
    this.installModules();
    final Command command = this.testFrameworkRule.getInstance(TestCommand.class);
    final CommandRegistry subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.addCommand(command);
    final Collection<Command> result = subjectUnderTest.getCommands();

    assertThat(result).containsOnly(command);
  }

  @Test
  public final void addCommandAndGetCommand_forCommand_mapsCommandToTrigger() {
    this.installModules();
    final Command command = this.testFrameworkRule.getInstance(TestCommand.class);
    final CommandRegistry subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.addCommand(command);
    final Optional<Command> result = subjectUnderTest.getCommand(command.getTrigger());

    assertThat(result).is(new Condition<Optional<Command>>() {

      @Override
      public boolean matches(final Optional<Command> value) {
        return value.isPresent() && value.get().equals(command);
      }

    });
  }

  @Test
  public final void hasCommand_nullTrigger_throwsNullPointerException() {
    this.installModules();
    final CommandRegistry subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.hasCommand(null));

    this.testFrameworkRule.assertNullPointerException(thrown, "trigger");
  }

  @Test
  public final void hasCommand_noCommandRegistered_resultIsFalse() {
    this.installModules();
    final CommandRegistry subjectUnderTest = this.givenASubjectToTest();

    final boolean result = subjectUnderTest.hasCommand(CommandTrigger.of(this.testFrameworkRule.getArbitraryString()));

    assertThat(result).isFalse();
  }

  @Test
  public final void hasCommand_commandRegistered_resultIsTrue() {
    this.installModules();
    final Command command = this.testFrameworkRule.getInstance(Command.class);
    final CommandRegistry subjectUnderTest = this.givenASubjectToTest();
    subjectUnderTest.addCommand(command);

    final boolean result = subjectUnderTest.hasCommand(command.getTrigger());

    assertThat(result).isTrue();
  }

  @Test
  public final void removeCommand_nullTrigger_throwsNullPointerException() {
    this.installModules();
    final CommandRegistry subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.removeCommand(null));

    this.testFrameworkRule.assertNullPointerException(thrown, "trigger");
  }

  @Test
  public final void removeCommand_noCommandRegistered_resultIsFalse() {
    this.installModules();
    final CommandRegistry subjectUnderTest = this.givenASubjectToTest();

    final boolean result = subjectUnderTest
        .removeCommand(CommandTrigger.of(this.testFrameworkRule.getArbitraryString()));

    assertThat(result).isFalse();
  }

  @Test
  public final void removeCommand_commandRegistered_resultIsTrue() {
    this.installModules();
    final Command command = this.testFrameworkRule.getInstance(Command.class);
    final CommandRegistry subjectUnderTest = this.givenASubjectToTest();
    subjectUnderTest.addCommand(command);

    final boolean result = subjectUnderTest.removeCommand(command.getTrigger());

    assertThat(result).isTrue();
  }

}
