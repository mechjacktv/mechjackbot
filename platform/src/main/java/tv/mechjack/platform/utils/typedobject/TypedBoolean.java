package tv.mechjack.platform.utils.typedobject;

public abstract class TypedBoolean extends StronglyTyped<Boolean> {

  public static <W extends TypedBoolean> W of(final Class<W> wrapper, final Boolean value) {
    return StronglyTyped.of(wrapper, value);
  }

  protected TypedBoolean(final Boolean value) {
    super(value);
  }

}
