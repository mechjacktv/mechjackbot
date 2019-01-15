package tv.mechjack.platform.util.function;

@FunctionalInterface
public interface RunnableWithException {

  void run() throws Exception;

}
