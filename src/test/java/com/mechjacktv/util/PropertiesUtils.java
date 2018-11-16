package com.mechjacktv.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

public final class PropertiesUtils {

  public final InputStream propertiesMapAsInputStream(final Map<String, String> properties) {
    final StringBuilder builder = new StringBuilder();

    for (final String key : properties.keySet()) {
      builder.append(String.format("%s = %s\n", key, properties.get(key)));
    }
    return new ByteArrayInputStream(builder.toString().getBytes());
  }

}
