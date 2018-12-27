package tv.mechjack.util;

import java.util.Arrays;
import java.util.Optional;

import org.assertj.core.api.Condition;

public final class OptionalByteArrayEqualsExpected extends Condition<Optional<byte[]>> {

  public OptionalByteArrayEqualsExpected(final byte[] expected) {
    super(actual -> actual.isPresent() && Arrays.equals(actual.get(), expected), "byte array equals");
  }

}
