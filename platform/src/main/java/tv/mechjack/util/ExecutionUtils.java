package tv.mechjack.util;

import tv.mechjack.util.function.RunnableWithException;
import tv.mechjack.util.function.SupplierWithException;

public interface ExecutionUtils {

  String nullMessageForName(String name);

  void softenException(RunnableWithException runnable, final Class<? extends RuntimeException> exceptionClass);

  <T> T softenException(SupplierWithException<T> supplier, final Class<? extends RuntimeException> exceptionClass);

}
