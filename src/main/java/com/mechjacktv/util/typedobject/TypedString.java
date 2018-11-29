package com.mechjacktv.util.typedobject;

public abstract class TypedString extends StronglyTyped<String> {

  public static <W extends TypedString> W of(final Class<W> wrapper, final String value) {
    return StronglyTyped.of(wrapper, value);
  }

  protected TypedString(final String value) {
    super(value);
  }

}
