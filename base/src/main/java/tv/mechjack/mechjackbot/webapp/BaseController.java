package tv.mechjack.mechjackbot.webapp;

import javax.servlet.http.HttpServletRequest;

public abstract class BaseController implements Controller {

  @Override
  public Object get(final HttpServletRequest request) {
    return new ErrorResponse(request.getRequestURI(),
        "405", "Method Not Allowed");
  }

  @Override
  public Object post(final HttpServletRequest request) {
    return new ErrorResponse(request.getRequestURI(),
        "405", "Method Not Allowed");
  }

  @Override
  public Object put(final HttpServletRequest request) {
    return new ErrorResponse(request.getRequestURI(),
        "405", "Method Not Allowed");
  }

  @Override
  public Object delete(final HttpServletRequest request) {
    return new ErrorResponse(request.getRequestURI(),
        "405", "Method Not Allowed");
  }

}
