package tv.mechjack.platform.utils;

import tv.mechjack.platform.utils.function.RunnableWithException;
import tv.mechjack.platform.utils.function.SupplierWithException;

public interface ExecutionUtils {

  String nullMessageForName(String name);

  void softenException(RunnableWithException runnable, final Class<? extends RuntimeException> exceptionClass);

  <T> T softenException(SupplierWithException<T> supplier, final Class<? extends RuntimeException> exceptionClass);

}
