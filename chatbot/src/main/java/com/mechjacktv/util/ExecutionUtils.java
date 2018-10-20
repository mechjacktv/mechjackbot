package com.mechjacktv.util;

public interface ExecutionUtils {

    void softenException(RunnableWithException runnalble);

    void softenException(RunnableWithException runnalble, final Class<? extends RuntimeException> exceptionClass );

    <T> T softenException(SupplierWithException<T> supplier);

    <T> T softenException(SupplierWithException<T> supplier, final Class<? extends RuntimeException> exceptionClass );

}
