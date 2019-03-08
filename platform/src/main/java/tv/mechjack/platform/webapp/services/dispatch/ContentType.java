package tv.mechjack.platform.webapp.services.dispatch;

import tv.mechjack.platform.utils.typedobject.TypedString;

public class ContentType extends TypedString {

  public static ContentType of(final String value) {
    return TypedString.of(ContentType.class, value);
  }

  private ContentType(final String value) {
    super(value);
  }

}
