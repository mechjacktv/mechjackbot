package tv.mechjack.util.typedobject;

public abstract class TypedDouble extends StronglyTyped<Double> {

  public static <W extends TypedDouble> W of(final Class<W> wrapper, final Double value) {
    return StronglyTyped.of(wrapper, value);
  }

  protected TypedDouble(final Double value) {
    super(value);
  }

}
