package tv.mechjack.platform.webapp.resource;

import tv.mechjack.platform.webapp.api.ContentType;
import tv.mechjack.platform.webapp.api.resource.UriPattern;

public final class DefaultResourceCommandComparator
    implements ResourceCommandComparator {

  public static final String URI_PATTERN_REGEX = "(\\{\\{[^}]+}}|\\([^)]+\\))";

  @Override
  public final int compare(final UriPattern uriPattern1,
      final UriPattern uriPattern2) {
    final int uriPatternDepthComparison = this.compareUriPatternDepth(
        uriPattern1, uriPattern2);

    if (uriPatternDepthComparison != 0) {
      return uriPatternDepthComparison;
    }

    final int uriPatternSpecificityComparison = this.compareUriPatternSpecificity(
        uriPattern1, uriPattern2);

    if (uriPatternSpecificityComparison != 0) {
      return uriPatternSpecificityComparison;
    }
    return 0;
  }

  private int compareUriPatternDepth(final UriPattern uriPattern1,
      final UriPattern uriPattern2) {
    final int uriPattern1Depth = uriPattern1.value.split("/").length;
    final int uriPatter2Depth = uriPattern2.value.split("/").length;

    return -(uriPattern1Depth - uriPatter2Depth);
  }

  private int compareUriPatternSpecificity(final UriPattern uriPattern1,
      final UriPattern uriPattern2) {
    final String[] uriPattern1Parts = uriPattern1.value.split("/");
    final String[] uriPattern2Parts = uriPattern2.value.split("/");

    for (int i = 0; i < uriPattern1Parts.length; i++) {
      final String uriPatternPart1 = uriPattern1Parts[i].replaceAll(URI_PATTERN_REGEX, "");
      final String uriPatternPart2 = uriPattern2Parts[i].replaceAll(URI_PATTERN_REGEX, "");
      final int partComparison = uriPatternPart1.length()
          - uriPatternPart2.length();

      if (partComparison != 0) {
        return -(partComparison);
      }
    }
    return 0;
  }

  @Override
  public final int compare(final ContentType contentType1,
      final ContentType contentType2) {
    if ("*/*".equals(contentType1.value)
        && !"*/*".equals(contentType2.value)) {
      return 1;
    } else if (!"*/*".equals(contentType1.value)
        && "*/*".equals(contentType2.value)) {
      return -1;
    }
    return contentType1.value.compareTo(contentType2.value);
  }

}
