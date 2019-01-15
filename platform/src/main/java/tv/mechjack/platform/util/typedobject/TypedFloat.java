package tv.mechjack.platform.util.typedobject;

public abstract class TypedFloat extends StronglyTyped<Float> {

  public static <W extends TypedFloat> W of(final Class<W> wrapper, final Float value) {
    return StronglyTyped.of(wrapper, value);
  }

  protected TypedFloat(final Float value) {
    super(value);
  }

}
