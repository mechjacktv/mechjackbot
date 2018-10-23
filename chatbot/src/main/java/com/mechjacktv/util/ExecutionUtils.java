package com.mechjacktv.util;

import com.mechjacktv.util.function.RunnableWithException;
import com.mechjacktv.util.function.SupplierWithException;

public interface ExecutionUtils {

  void softenException(RunnableWithException runnable);

  void softenException(RunnableWithException runnable, final Class<? extends RuntimeException> exceptionClass);

  <T> T softenException(SupplierWithException<T> supplier);

  <T> T softenException(SupplierWithException<T> supplier, final Class<? extends RuntimeException> exceptionClass);

}
