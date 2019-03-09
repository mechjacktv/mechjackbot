package tv.mechjack.platform.webapp.resource;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import tv.mechjack.platform.webapp.api.resource.ResponseHandler;

public interface ResponseHandlerRegistry {

  Optional<ResponseHandler> findResponseHandler(HttpServletRequest request,
      Object resource);

}
