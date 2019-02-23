package tv.mechjack.mechjackbot.webapp;

import javax.servlet.http.HttpServletRequest;

public interface Controller {

  Object get(HttpServletRequest request);

  Object post(HttpServletRequest request);

  Object put(HttpServletRequest request);

  Object delete(HttpServletRequest request);

}
