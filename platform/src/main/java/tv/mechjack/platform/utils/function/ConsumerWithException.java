package tv.mechjack.platform.utils.function;

@FunctionalInterface
public interface ConsumerWithException<T> {

  void accept(final T t) throws Exception;

}
