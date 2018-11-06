package com.mechjacktv.util.function;

@FunctionalInterface
public interface RunnableWithException {

  void run() throws Exception;

}
