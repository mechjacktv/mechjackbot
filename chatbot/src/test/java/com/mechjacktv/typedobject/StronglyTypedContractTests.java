package com.mechjacktv.typedobject;

import static org.assertj.core.api.Assertions.*;
import org.junit.Test;

public abstract class StronglyTypedContractTests<T> {

  @Test
  public final void new_notNullValue_wrapsValue() {
    final T value = givenIHaveAValue();

    final StronglyTyped result = givenIHaveAStronglyTypedValue(value);

    assertThat(result.value).isEqualTo(value);
  }

  @Test
  public final void new_nullValue_throwsNullPointerException() {
    final Throwable result = catchThrowable(() -> givenIHaveAStronglyTypedValue(null));

    assertThat(result).isInstanceOf(NullPointerException.class);
  }

  protected abstract T givenIHaveAValue();

  protected abstract StronglyTyped givenIHaveAStronglyTypedValue(final T value);

}
