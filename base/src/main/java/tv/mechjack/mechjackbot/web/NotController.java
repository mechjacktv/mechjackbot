package tv.mechjack.mechjackbot.web;

import javax.servlet.http.HttpServletRequest;

public interface NotController {

  Object get(HttpServletRequest request);

  Object post(HttpServletRequest request);

  Object put(HttpServletRequest request);

  Object delete(HttpServletRequest request);

}
