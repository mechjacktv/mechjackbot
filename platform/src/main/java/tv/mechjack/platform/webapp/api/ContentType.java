package tv.mechjack.platform.webapp.api;

import tv.mechjack.platform.utils.typedobject.TypedString;

public class ContentType extends TypedString {

  public static final ContentType ANY = ContentType.of("*/*");
  public static final ContentType CSS = ContentType.of("text/css");
  public static final ContentType CSV = ContentType.of("text/csv");
  public static final ContentType HTML = ContentType.of("text/html");
  public static final ContentType JAVA_SCRIPT = ContentType.of("text/javascript");
  public static final ContentType JSON = ContentType.of("application/json");
  public static final ContentType OCTET_STREAM = ContentType.of("application/octet-stream");
  public static final ContentType SVG = ContentType.of("image/svg+xml");
  public static final ContentType TEXT = ContentType.of("text/plain");

  public static ContentType of(final String value) {
    return TypedString.of(ContentType.class, value);
  }

  private ContentType(final String value) {
    super(value);
  }

}
