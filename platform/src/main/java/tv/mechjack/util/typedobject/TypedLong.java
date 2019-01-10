package tv.mechjack.util.typedobject;

public abstract class TypedLong extends StronglyTyped<Long> {

  public static <W extends TypedLong> W of(final Class<W> wrapper, final Long value) {
    return StronglyTyped.of(wrapper, value);
  }

  protected TypedLong(final Long value) {
    super(value);
  }

}
