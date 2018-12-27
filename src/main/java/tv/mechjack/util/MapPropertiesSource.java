package tv.mechjack.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import tv.mechjack.util.function.ConsumerWithException;

public final class MapPropertiesSource implements PropertiesSource {

  private final Map<String, String> data;

  public MapPropertiesSource(final Map<String, String> data) {
    this.data = new HashMap<>(data);
  }

  @Override
  public void read(final ConsumerWithException<InputStream> propertiesLoader) throws Exception {
    final StringBuilder builder = new StringBuilder();

    for (final String key : this.data.keySet()) {
      builder.append(String.format("%s = %s\n", key, this.data.get(key)));
    }
    try (final ByteArrayInputStream inputStream = new ByteArrayInputStream(builder.toString().getBytes())) {
      propertiesLoader.accept(inputStream);
    }
  }

}
