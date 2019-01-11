package tv.mechjack.mechjackbot.core;

import java.util.List;

import picocli.CommandLine.Model.OptionSpec;
import picocli.CommandLine.Model.PositionalParamSpec;

import tv.mechjack.mechjackbot.api.PicoCliUtils;

public final class DefaultPicoCliUtils implements PicoCliUtils {

  @Override
  public OptionSpec createStringOption(final boolean required, final String name,
      final String... names) {
    return OptionSpec.builder(name, names)
        .required(required)
        .type(String.class)
        .build();
  }

  @Override
  public PositionalParamSpec createStringParam(final boolean required, final String index) {
    return PositionalParamSpec.builder()
        .required(required)
        .index(index)
        .type(String.class)
        .build();
  }

  @Override
  public PositionalParamSpec createStringListParam(final boolean required,
      final String index) {
    return PositionalParamSpec.builder()
        .required(required)
        .index(index)
        .type(List.class)
        .auxiliaryTypes(String.class)
        .build();
  }

}
