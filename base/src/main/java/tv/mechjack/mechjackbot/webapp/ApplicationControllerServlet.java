package tv.mechjack.mechjackbot.webapp;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.gson.Gson;

@Singleton
public class ApplicationControllerServlet extends ControllerServlet {

  @Inject
  ApplicationControllerServlet(final Gson gson, final ApplicationController controller) {
    super(gson, controller);
  }

}
