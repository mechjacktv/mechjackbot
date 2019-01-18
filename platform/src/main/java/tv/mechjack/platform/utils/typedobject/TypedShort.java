package tv.mechjack.platform.utils.typedobject;

public abstract class TypedShort extends StronglyTyped<Short> {

  public static <W extends TypedShort> W of(final Class<W> wrapper, final Short value) {
    return StronglyTyped.of(wrapper, value);
  }

  protected TypedShort(final Short value) {
    super(value);
  }

}
