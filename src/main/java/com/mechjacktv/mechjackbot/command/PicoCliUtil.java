package com.mechjacktv.mechjackbot.command;

import java.util.List;

import picocli.CommandLine.Model.OptionSpec;
import picocli.CommandLine.Model.PositionalParamSpec;

public final class PicoCliUtil {

  private PicoCliUtil() {
    /* no-op (2018-12-23 mechjack) prevents instantiation */
  }

  public static OptionSpec createStringOption(final boolean required, final String name,
      final String... names) {
    return OptionSpec.builder(name, names)
        .required(required)
        .type(String.class)
        .build();
  }

  public static PositionalParamSpec createStringParam(final boolean required, final String index) {
    return PositionalParamSpec.builder()
        .required(required)
        .index(index)
        .type(String.class)
        .build();
  }

  public static PositionalParamSpec createStringListParam(final boolean required,
      final String index) {
    return PositionalParamSpec.builder()
        .required(required)
        .index(index)
        .type(List.class)
        .auxiliaryTypes(String.class)
        .build();
  }

}
