package com.mechjacktv.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mechjacktv.util.function.RunnableWithException;
import com.mechjacktv.util.function.SupplierWithException;

public final class DefaultExecutionUtils implements ExecutionUtils {

  private static final Logger log = LoggerFactory.getLogger(DefaultExecutionUtils.class);

  @Override
  public String nullMessageForName(final String name) {
    return String.format("`%s` **MUST** not be `null`", name);
  }

  @Override
  public final void softenException(final RunnableWithException runnable,
      final Class<? extends RuntimeException> exceptionClass) {
    this.softenException(() -> {
      runnable.run();
      return null;
    }, exceptionClass);
  }

  @Override
  public final <T> T softenException(final SupplierWithException<T> supplier,
      final Class<? extends RuntimeException> exceptionClass) {
    try {
      return supplier.get();
    } catch (final Exception e1) {
      if (RuntimeException.class.isAssignableFrom(e1.getClass())) {
        throw (RuntimeException) e1;
      }
      try {
        final Constructor<? extends RuntimeException> constructor = exceptionClass
            .getConstructor(String.class, Throwable.class);

        throw constructor.newInstance(e1.getMessage(), e1);
      } catch (final NoSuchMethodException | InstantiationException | IllegalAccessException
          | IllegalArgumentException | InvocationTargetException e2) {
        log.error(String.format("Failed to instantiate RuntimeException. exceptionClass=%s, ",
            exceptionClass.getCanonicalName()), e2);
        throw new SoftenedException(e1.getMessage(), e1);
      }
    }
  }

}
