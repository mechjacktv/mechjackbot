package tv.mechjack.platform.utils.typedobject;

public abstract class TypedCharacter extends StronglyTyped<Character> {

  public static <W extends TypedCharacter> W of(final Class<W> wrapper, final Character value) {
    return StronglyTyped.of(wrapper, value);
  }

  protected TypedCharacter(final Character value) {
    super(value);
  }

}
