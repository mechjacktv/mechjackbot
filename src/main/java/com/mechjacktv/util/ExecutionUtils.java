package com.mechjacktv.util;

import com.mechjacktv.util.function.RunnableWithException;
import com.mechjacktv.util.function.SupplierWithException;

public interface ExecutionUtils {

  String nullMessageForName(String name);

  void softenException(RunnableWithException runnable, final Class<? extends RuntimeException> exceptionClass);

  <T> T softenException(SupplierWithException<T> supplier, final Class<? extends RuntimeException> exceptionClass);

}
