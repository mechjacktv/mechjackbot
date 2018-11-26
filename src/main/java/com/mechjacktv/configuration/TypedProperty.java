package com.mechjacktv.configuration;

import java.util.Optional;

import com.mechjacktv.guice.InjectorBridge;

public abstract class TypedProperty<X> {

  private final InjectorBridge injectorBridge;
  private String key;
  private final X defaultValue;

  protected TypedProperty(final Class<?> container, final String name, final X defaultValue) {
    this(InjectorBridge.getBridge(), container, name, defaultValue);
  }

  TypedProperty(final InjectorBridge injectorBridge, final Class<?> container,
      final String name, final X defaultValue) {
    this.injectorBridge = injectorBridge;
    this.key = String.format("%s.%s", container.getCanonicalName(), name);
    this.defaultValue = defaultValue;
  }

  public final String getKey() {
    return this.key;
  }

  public final X getDefaultValue() {
    return this.defaultValue;
  }

  public final X getValue() {
    final Configuration configuration = this.injectorBridge.getInstance(Configuration.class);
    final Optional<String> value = configuration.get(this.key);

    return value.isPresent() ? this.parseValue(value.get()) : this.defaultValue;
  }

  protected abstract X parseValue(String value);

}
