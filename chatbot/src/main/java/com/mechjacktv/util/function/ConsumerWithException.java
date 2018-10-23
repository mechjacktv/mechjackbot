package com.mechjacktv.util.function;

@FunctionalInterface
public interface ConsumerWithException<T> {

    void accept(final T t) throws Exception;

}
