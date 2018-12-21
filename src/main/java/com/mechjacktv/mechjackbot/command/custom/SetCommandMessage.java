package com.mechjacktv.mechjackbot.command.custom;

import java.util.Objects;
import java.util.Optional;

import com.mechjacktv.mechjackbot.AccessLevel;
import com.mechjacktv.mechjackbot.CommandTrigger;

import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

public class SetCommandMessage {

  @Option(names = {"-a", "--access-level"})
  private String rawAccessLevel = null;

  @Parameters(index = "1..*")
  private String[] rawBodyPart = null;

  @Parameters(index = "0")
  private String rawTrigger = null;

  private AccessLevel accessLevel = null;

  private CommandBody body = null;

  private CommandTrigger trigger = null;

  public Optional<AccessLevel> getAccessLevel() {
    return Optional.ofNullable(this.accessLevel);
  }

  public Optional<CommandBody> getBody() {
    return Optional.ofNullable(this.body);
  }

  public CommandTrigger getTrigger() {
    return this.trigger;
  }

  public void validate() {
    Objects.nonNull(this.rawTrigger);
    this.trigger = CommandTrigger.of(this.rawTrigger);
    if(Objects.nonNull(this.rawAccessLevel)) {
      this.accessLevel = AccessLevel.valueOf(this.rawAccessLevel.toUpperCase());
    }
    if(Objects.nonNull(this.rawBodyPart)) {
      this.body = CommandBody.of(String.join(" ", this.rawBodyPart));
    }
  }

}
