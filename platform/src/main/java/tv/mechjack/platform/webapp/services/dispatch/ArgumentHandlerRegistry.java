package tv.mechjack.platform.webapp.services.dispatch;

import java.lang.annotation.Annotation;
import java.util.Optional;

import tv.mechjack.platform.webapp.services.ArgumentHandler;

public interface ArgumentHandlerRegistry {

  Optional<ArgumentHandler> findArgumentHandler(Class<?> argument,
      Annotation[] annotations);

}
