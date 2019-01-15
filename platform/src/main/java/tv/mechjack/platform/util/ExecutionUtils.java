package tv.mechjack.platform.util;

import tv.mechjack.platform.util.function.RunnableWithException;
import tv.mechjack.platform.util.function.SupplierWithException;

public interface ExecutionUtils {

  String nullMessageForName(String name);

  void softenException(RunnableWithException runnable, final Class<? extends RuntimeException> exceptionClass);

  <T> T softenException(SupplierWithException<T> supplier, final Class<? extends RuntimeException> exceptionClass);

}
