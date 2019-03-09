package tv.mechjack.platform.webapp.resource;

import java.lang.annotation.Annotation;
import java.util.Optional;

import tv.mechjack.platform.webapp.api.resource.ArgumentHandler;

public interface ArgumentHandlerRegistry {

  Optional<ArgumentHandler> findArgumentHandler(Class<?> argument,
      Annotation[] annotations);

}
