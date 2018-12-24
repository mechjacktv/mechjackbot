package com.mechjacktv.mechjackbot.command.custom;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;

import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import com.mechjacktv.mechjackbot.ChatCommandTrigger;
import com.mechjacktv.mechjackbot.UserRole;

public class SetCommandMessage implements Callable<SetCommandMessage> {

  @Option(names = { "-a", "--access-level" })
  private String rawAccessLevel = null;

  @Parameters(index = "1..*")
  private String[] rawBodyPart = null;

  @Parameters(index = "0")
  private String rawTrigger = null;

  private UserRole accessLevel = null;

  private CommandBody body = null;

  private ChatCommandTrigger trigger = null;

  public Optional<UserRole> getAccessLevel() {
    return Optional.ofNullable(this.accessLevel);
  }

  public Optional<CommandBody> getBody() {
    return Optional.ofNullable(this.body);
  }

  public ChatCommandTrigger getTrigger() {
    return this.trigger;
  }

  public void validate() {
    Objects.nonNull(this.rawTrigger);
    this.trigger = ChatCommandTrigger.of(this.rawTrigger);
    if (Objects.nonNull(this.rawAccessLevel)) {
      this.accessLevel = UserRole.valueOf(this.rawAccessLevel.toUpperCase());
    }
    if (Objects.nonNull(this.rawBodyPart)) {
      this.body = CommandBody.of(String.join(" ", this.rawBodyPart));
    }
  }

  @Override
  public SetCommandMessage call() {
    this.validate();
    return this;
  }

}
