package tv.mechjack.platform.webapp.api.exception;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import tv.mechjack.platform.webapp.api.HttpStatusCode;

public abstract class HttpErrorStatusException extends RuntimeException {

  private final Map<String, String> headers;
  private final HttpStatusCode statusCode;

  public HttpErrorStatusException(final HttpStatusCode statusCode, final String message) {
    this(statusCode, message, null);
  }

  public HttpErrorStatusException(final HttpStatusCode statusCode, final String message,
      final Throwable cause) {
    super(message, cause);
    this.headers = new HashMap<>();
    this.statusCode = statusCode;
  }

  public final Set<String> getHeaderNames() {
    return Collections.unmodifiableSet(this.headers.keySet());
  }

  public final String getHeaderValue(final String name) {
    return this.headers.get(name);
  }

  public final void setHeader(final String name, final String value) {
    this.headers.put(name, value);
  }

  public final HttpStatusCode getStatusCode() {
    return this.statusCode;
  }

}
