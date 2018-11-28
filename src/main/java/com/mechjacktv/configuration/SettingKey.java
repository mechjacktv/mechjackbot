package com.mechjacktv.configuration;

import com.mechjacktv.util.typedobject.TypedString;

public class SettingKey extends TypedString {

  private SettingKey(final String value) {
    super(value);
  }

  public static SettingKey of(final Class<?> scope, final String value) {
    return new SettingKey(String.format("%s.%s", scope.getCanonicalName().toLowerCase(), value));
  }

}
