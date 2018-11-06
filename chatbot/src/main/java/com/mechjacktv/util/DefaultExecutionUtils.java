package com.mechjacktv.util;

import java.lang.reflect.Constructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mechjacktv.util.function.RunnableWithException;
import com.mechjacktv.util.function.SupplierWithException;

public final class DefaultExecutionUtils implements ExecutionUtils {

  private static final Logger log = LoggerFactory.getLogger(DefaultExecutionUtils.class);

  @Override
  public final void softenException(RunnableWithException runnable, Class<? extends RuntimeException> exceptionClass) {
    this.softenException(() -> {
      runnable.run();
      return null;
    }, exceptionClass);
  }

  @Override
  public final <T> T softenException(SupplierWithException<T> supplier,
      Class<? extends RuntimeException> exceptionClass) {
    try {
      return supplier.get();
    } catch (final RuntimeException e) {
      throw e;
    } catch (final Exception e1) {
      try {
        try {
          final Constructor<? extends RuntimeException> constructor = exceptionClass
              .getConstructor(String.class, Throwable.class);

          throw constructor.newInstance(e1.getMessage(), e1);
        } catch (final NoSuchMethodException e2) {
          final Constructor<? extends RuntimeException> constructor = exceptionClass
              .getConstructor(Throwable.class);

          throw constructor.newInstance(e1);
        }
      } catch (final RuntimeException e2) {
        throw e2;
      } catch (final Exception e2) {
        log.error(String.format("Failed to instantiate RuntimeException. exceptionClass=%s, ",
            exceptionClass.getCanonicalName()), e2);
        throw new SoftenedException(e1.getMessage(), e1);
      }
    }
  }

}
