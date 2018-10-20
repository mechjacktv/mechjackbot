package com.mechjacktv.util;

@FunctionalInterface
public interface SupplierWithException<T> {

    T get() throws Exception;

}
