package tv.mechjack.mechjackbot.feature.autotrigger;

import tv.mechjack.platform.utils.typedobject.TypedString;

public class ListName extends TypedString {

  private ListName(final String value) {
    super(value);
  }

  public static ListName of(final String value) {
    return TypedString.of(ListName.class, value);
  }

}
