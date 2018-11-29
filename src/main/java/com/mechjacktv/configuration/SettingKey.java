package com.mechjacktv.configuration;

import com.mechjacktv.util.typedobject.TypedString;

public class SettingKey extends TypedString {

  public static SettingKey of(final String value, final Class<?> scope) {
    return TypedString.of(SettingKey.class, String.format("%s.%s", scope.getCanonicalName(), value).toLowerCase());
  }

  private SettingKey(final String value) {
    super(value);
  }

}
