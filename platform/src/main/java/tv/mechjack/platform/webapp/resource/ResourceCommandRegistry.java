package tv.mechjack.platform.webapp.resource;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

public interface ResourceCommandRegistry {

  Optional<ResourceCommand> findResourceCommand(
      HttpServletRequest request);

}
