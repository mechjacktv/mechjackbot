package tv.mechjack.mechjackbot.web;

import javax.servlet.http.HttpServletRequest;

public abstract class BaseController implements NotController {

  @Override
  public Object get(final HttpServletRequest request) {
    return new ErrorResponse(request.getRequestURI(),
        "405", "HttpMethod Not Allowed");
  }

  @Override
  public Object post(final HttpServletRequest request) {
    return new ErrorResponse(request.getRequestURI(),
        "405", "HttpMethod Not Allowed");
  }

  @Override
  public Object put(final HttpServletRequest request) {
    return new ErrorResponse(request.getRequestURI(),
        "405", "HttpMethod Not Allowed");
  }

  @Override
  public Object delete(final HttpServletRequest request) {
    return new ErrorResponse(request.getRequestURI(),
        "405", "HttpMethod Not Allowed");
  }

}
