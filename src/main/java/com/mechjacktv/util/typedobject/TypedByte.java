package com.mechjacktv.util.typedobject;

public abstract class TypedByte extends StronglyTyped<Byte> {

  public static <W extends TypedByte> W of(final Class<W> wrapper, final Byte value) {
    return StronglyTyped.of(wrapper, value);
  }

  protected TypedByte(final Byte value) {
    super(value);
  }

}
