package tv.mechjack.platform.utils.function;

@FunctionalInterface
public interface RunnableWithException {

  void run() throws Exception;

}
