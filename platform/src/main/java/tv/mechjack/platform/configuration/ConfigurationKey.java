package tv.mechjack.platform.configuration;

import tv.mechjack.platform.utils.typedobject.TypedString;

public final class ConfigurationKey extends TypedString {

  public static ConfigurationKey of(final String value) {
    return TypedString.of(ConfigurationKey.class, value.toLowerCase());
  }

  public static ConfigurationKey of(final String value, final Class<?> scope) {
    return ConfigurationKey.of(String.format("%s.%s", scope.getCanonicalName().replace('.', '_'), value));
  }

  private ConfigurationKey(final String value) {
    super(value);
  }

}
