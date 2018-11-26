package com.mechjacktv.mechjackbot.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Optional;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.CommandTrigger;
import com.mechjacktv.util.ArbitraryDataGenerator;
import com.mechjacktv.util.DefaultExecutionUtils;
import com.mechjacktv.util.ExecutionUtils;

public class DefaultCommandRegistryUnitTests {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();
  private final ExecutionUtils executionUtils = new DefaultExecutionUtils();

  private DefaultCommandRegistry givenASubjectToTest() {
    return new DefaultCommandRegistry(this.executionUtils);
  }

  private Command givenAFakeCommand() {
    return this.givenAFakeCommand(CommandTrigger.of(this.arbitraryDataGenerator.getString()));
  }

  private Command givenAFakeCommand(final CommandTrigger commandTrigger) {
    final Command command = mock(Command.class);

    when(command.getTrigger()).thenReturn(commandTrigger);
    return command;
  }

  @Test
  public final void addCommand_nullCommand_throwsNullPointerExceptionWithMessage() {
    final DefaultCommandRegistry subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.addCommand(null));

    assertThat(thrown).isInstanceOf(NullPointerException.class)
        .hasMessage(this.executionUtils.nullMessageForName("command"));
  }

  @Test
  public final void addCommandAndGetCommands_forCommand_storesCommand() {
    final Command command = this.givenAFakeCommand();
    final DefaultCommandRegistry subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.addCommand(command);
    final Collection<Command> result = subjectUnderTest.getCommands();

    assertThat(result).containsOnly(command);
  }

  @Test
  public final void addCommandAndGetCommand_forCommand_mapsCommandToTrigger() {
    final CommandTrigger trigger = CommandTrigger.of(this.arbitraryDataGenerator.getString());
    final Command command = this.givenAFakeCommand(trigger);
    final DefaultCommandRegistry subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.addCommand(command);
    final Optional<Command> result = subjectUnderTest.getCommand(trigger);

    final SoftAssertions softly = new SoftAssertions();
    softly.assertThat(result).isNotEmpty();
    result.ifPresent((resultCommand) -> softly.assertThat(resultCommand).isEqualTo(command));
    softly.assertAll();
  }

}
