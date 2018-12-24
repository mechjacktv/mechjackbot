package com.mechjacktv.mechjackbot.command.custom;

import java.util.List;

import picocli.CommandLine.Model.OptionSpec;
import picocli.CommandLine.Model.PositionalParamSpec;

public final class PicoCliUtil {

  public static OptionSpec createUserRoleOptionSpec(final boolean required) {
    return OptionSpec.builder("-r", "--user-role")
        .required(required)
        .type(String.class)
        .build();
  }

  public static PositionalParamSpec createTriggerPositionalParamSpec(final boolean required) {
    return PositionalParamSpec.builder()
        .required(required)
        .index("0")
        .type(String.class)
        .build();
  }

  public static PositionalParamSpec createBodyPositionalParamSpec(final boolean required) {
    return PositionalParamSpec.builder()
        .required(required)
        .index("1..*")
        .type(List.class)
        .auxiliaryTypes(String.class)
        .build();
  }

}
