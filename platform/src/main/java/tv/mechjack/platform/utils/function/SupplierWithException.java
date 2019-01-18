package tv.mechjack.platform.utils.function;

@FunctionalInterface
public interface SupplierWithException<T> {

  T get() throws Exception;

}
