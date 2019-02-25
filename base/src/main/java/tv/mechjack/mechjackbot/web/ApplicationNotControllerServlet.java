package tv.mechjack.mechjackbot.web;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.gson.Gson;

@Singleton
public class ApplicationNotControllerServlet extends NotControllerServlet {

  @Inject
  ApplicationNotControllerServlet(final Gson gson, final ApplicationNotController controller) {
    super(gson, controller);
  }

}
