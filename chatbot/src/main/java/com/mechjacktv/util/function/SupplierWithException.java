package com.mechjacktv.util.function;

@FunctionalInterface
public interface SupplierWithException<T> {

    T get() throws Exception;

}
