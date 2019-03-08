package tv.mechjack.platform.webapp.services;

import tv.mechjack.platform.utils.typedobject.TypedString;

public class UriPattern extends TypedString {

  public static UriPattern of(final String value) {
    return TypedString.of(UriPattern.class, getSanitizedValue(value.trim()));
  }

  private static String getSanitizedValue(final String value) {
    if (value.endsWith("/")) {
      return value.substring(0, value.length() - 1);
    }
    return value;
  }

  private UriPattern(final String value) {
    super(value);
  }

}
