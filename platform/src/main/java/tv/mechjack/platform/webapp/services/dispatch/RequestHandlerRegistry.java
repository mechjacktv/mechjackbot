package tv.mechjack.platform.webapp.services.dispatch;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

public interface RequestHandlerRegistry {

  Optional<RequestHandler> findRequestHandler(
      HttpServletRequest request);

}
