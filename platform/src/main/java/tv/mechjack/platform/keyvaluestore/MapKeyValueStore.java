package tv.mechjack.platform.keyvaluestore;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public final class MapKeyValueStore implements KeyValueStore {

  private static final Base64.Decoder DECODER = Base64.getDecoder();
  private static final Base64.Encoder ENCODER = Base64.getEncoder();
  private static final Charset UTF_8_CHARSET = Charset.forName("UTF-8");

  private final Map<String, String> dataMap;

  public MapKeyValueStore() {
    this(new HashMap<>());
  }

  public MapKeyValueStore(final Map<String, String> dataMap) {
    this.dataMap = dataMap;
  }

  @Override
  public boolean containsKey(final byte[] key) {
    Objects.requireNonNull(key, "`key` **MUST** not be `null`");

    return this.dataMap.containsKey(this.encode(key));
  }

  @Override
  public Collection<byte[]> getKeys() {
    return Collections.unmodifiableSet(this.dataMap.keySet().stream()
        .map(this::decode).collect(Collectors.toSet()));
  }

  @Override
  public Optional<byte[]> get(byte[] key) {
    Objects.requireNonNull(key, "`key` **MUST** not be `null`");

    return Optional.ofNullable(this.decode(this.dataMap.get(this.encode(key))));
  }

  @Override
  public void put(byte[] key, byte[] value) {
    Objects.requireNonNull(key, "`key` **MUST** not be `null`");
    Objects.requireNonNull(value, "`value` **MUST** not be `null`");

    if (this.dataMap.containsKey(this.encode(key))) {
      this.dataMap.replace(this.encode(key), this.encode(value));
    } else {
      this.dataMap.put(this.encode(key), this.encode(value));
    }
  }

  @Override
  public void remove(byte[] key) {
    Objects.requireNonNull(key, "`key` **MUST** not be `null`");

    this.dataMap.remove(this.encode(key));
  }

  private byte[] decode(final String data) {
    if (Objects.isNull(data)) {
      return null;
    }
    return DECODER.decode(data.getBytes(UTF_8_CHARSET));
  }

  private String encode(final byte[] data) {
    if (Objects.isNull(data)) {
      return null;
    }
    return new String(ENCODER.encode(data), UTF_8_CHARSET);
  }

}
