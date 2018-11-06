package com.mechjacktv.typedobject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.Test;

public abstract class StronglyTypedContractTests<T> {

  @Test
  public final void new_notNullValue_wrapsValue() {
    final T value = this.givenIHaveAValue();

    final StronglyTyped result = this.givenIHaveAStronglyTypedValue(value);

    assertThat(result.value).isEqualTo(value);
  }

  @Test
  public final void new_nullValue_throwsNullPointerException() {
    final Throwable result = catchThrowable(() -> this.givenIHaveAStronglyTypedValue(null));

    assertThat(result).isInstanceOf(NullPointerException.class);
  }

  protected abstract T givenIHaveAValue();

  protected abstract StronglyTyped givenIHaveAStronglyTypedValue(final T value);

}
