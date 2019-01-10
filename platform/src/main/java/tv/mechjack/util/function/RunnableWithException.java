package tv.mechjack.util.function;

@FunctionalInterface
public interface RunnableWithException {

  void run() throws Exception;

}
