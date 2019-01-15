package tv.mechjack.platform.util.typedobject;

public abstract class TypedInteger extends StronglyTyped<Integer> {

  public static <W extends TypedInteger> W of(final Class<W> wrapper, final Integer value) {
    return StronglyTyped.of(wrapper, value);
  }

  protected TypedInteger(final Integer value) {
    super(value);
  }

}
