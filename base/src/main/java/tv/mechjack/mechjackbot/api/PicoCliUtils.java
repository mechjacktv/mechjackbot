package tv.mechjack.mechjackbot.api;

import picocli.CommandLine.Model.OptionSpec;
import picocli.CommandLine.Model.PositionalParamSpec;

public interface PicoCliUtils {

  OptionSpec createStringOption(boolean required, String name,
      String... names);

  PositionalParamSpec createStringParam(boolean required, String index);

  PositionalParamSpec createStringListParam(boolean required,
      String index);

}
