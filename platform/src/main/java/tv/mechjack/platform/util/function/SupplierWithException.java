package tv.mechjack.platform.util.function;

@FunctionalInterface
public interface SupplierWithException<T> {

  T get() throws Exception;

}
