package com.mechjacktv.configuration;

import com.mechjacktv.util.typedobject.TypedString;

public final class ConfigurationKey extends TypedString {

  public static ConfigurationKey of(final String value) {
    return TypedString.of(ConfigurationKey.class, value.toLowerCase());
  }

  public static ConfigurationKey of(final String value, final Class<?> scope) {
    return ConfigurationKey.of(String.format("%s#%s", scope.getCanonicalName(), value));
  }

  private ConfigurationKey(final String value) {
    super(value);
  }

}
