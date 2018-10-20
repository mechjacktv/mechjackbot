package com.mechjacktv.util;

@FunctionalInterface
public interface RunnableWithException {

    void run() throws Exception;

}
