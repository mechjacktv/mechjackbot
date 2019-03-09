package tv.mechjack.platform.webapp.resource;

import tv.mechjack.platform.webapp.api.ContentType;
import tv.mechjack.platform.webapp.api.resource.UriPattern;

public interface ResourceCommandComparator {

  int compare(UriPattern uriPattern1, UriPattern uriPattern2);

  int compare(ContentType contentType1, ContentType contentType2);

}
