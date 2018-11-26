package com.mechjacktv.configuration;

public final class StringProperty extends TypedProperty<String> {

  public StringProperty(final Class<?> container, final String name, final String defaultValue) {
    super(container, name, defaultValue);
  }

  @Override
  protected String parseValue(final String value) {
    return value;
  }

}
