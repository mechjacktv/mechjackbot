package com.mechjacktv.typedobject;

import java.util.Objects;

/**
 * Abstract base class for wrapping commonly used types with a strongly typed
 * wrapper. Take for example the following code:
 * <p>
 * ```java public final class User {
 * <p>
 * private final String firstName;
 * <p>
 * private final String lastName;
 * <p>
 * public User(final String firstName, final String lastName) { this.firstName =
 * firstName; this.lastName = lastName; }
 * <p>
 * public final String toString() { return String.format("%s, %s", lastName,
 * firstName); }
 * <p>
 * } ```
 * <p>
 * Because of the simplicity of the code, it's easy to keep track of which
 * `String` is the `firstName` and which is the `lastName`. But any code that
 * constructs a new `User` definitely needs to make sure that they are putting
 * the `String` in the correct order.
 * <p>
 * Compare this to the following code:
 * <p>
 * ```java public final class User {
 * <p>
 * private final FirstName firstName;
 * <p>
 * private final LastName lastName;
 * <p>
 * public User(final FirstName firstName, final LastName lastName) {
 * this.firstName = firstName; this.lastName = lastName; }
 * <p>
 * public final String toString() { return String.format("%s, %s",
 * lastName.value, firstName.value); }
 * <p>
 * } ```
 * <p>
 * Because the `String`s are wrapped in a strongly typed wrapper class it's
 * impossible to pass them values in in the wrong order while constructing a new
 * `User` instance. Because the wrapped value is exposed as a public final
 * member variable it's easy and cheap to access it when the raw value is needed
 * directly.
 *
 * @param <T> the `Type` to wrap
 */
public abstract class StronglyTyped<T> {

  /**
   * The wrapped `value`.
   * <p>
   * **Note:** Because this is just an identifying wrapper around a common type
   * there no real reason to enforce encapsulation. The only thing we're ever
   * going to do is access the value and if we're smart the value is as immutable
   * as this class.
   */
  public final T value;

  /**
   * Constructs a new wrapper for the passed in `value`.
   *
   * @param value the `value` to wrap
   */
  StronglyTyped(final T value) {
    Objects.requireNonNull(value, "A `StronglyTyped` `value` **MUST NOT** be `null`");
    this.value = value;
  }

  @Override
  public String toString() {
    return this.value.toString();
  }

  @Override
  public boolean equals(final Object obj) {
    if (Objects.isNull(obj)) {
      return false;
    } else if (!this.getClass().isAssignableFrom(obj.getClass())) {
      return false;
    } else {
      return this.value.equals(((StronglyTyped) obj).value);
    }
  }

  @Override
  public int hashCode() {
    return this.value.hashCode();
  }

}
