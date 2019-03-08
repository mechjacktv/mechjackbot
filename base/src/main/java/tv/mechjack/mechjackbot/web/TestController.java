package tv.mechjack.mechjackbot.web;

import tv.mechjack.platform.webapp.services.Action;
import tv.mechjack.platform.webapp.services.Action.Type;
import tv.mechjack.platform.webapp.services.Controller;
import tv.mechjack.platform.webapp.services.HttpMethod;
import tv.mechjack.platform.webapp.services.Parameter;
import tv.mechjack.platform.webapp.services.Service;
import tv.mechjack.platform.webapp.services.UriPattern;

public class TestController implements Controller {

  @Override
  public UriPattern getUriRootPattern() {
    return UriPattern.of("/api/v1/test");
  }

  @Action(type = Type.LIST)
  public GreetingMessage sayHelloList() {
    final GreetingMessage message = new GreetingMessage();

    message.setGreeting("Hello, listed world!");
    return message;
  }

  @Action(type = Type.CREATE)
  public GreetingMessage sayHelloCreate(final ServiceMessage serviceMessage) {
    final GreetingMessage message = new GreetingMessage();

    message.setGreeting("Hello, created " + serviceMessage.getValue() + "!");
    return message;
  }

  @Action(type = Type.SHOW)
  public GreetingMessage sayHelloShow(final @Parameter("id") String id) {
    final GreetingMessage message = new GreetingMessage();

    message.setGreeting("Hello, shown " + id + "!");
    return message;
  }

  @Action(type = Type.UPDATE)
  public GreetingMessage sayHelloUpdate(final @Parameter("id") String id) {
    final GreetingMessage message = new GreetingMessage();

    message.setGreeting("Hello, updated " + id + "!");
    return message;
  }

  @Action(type = Type.DESTROY)
  public GreetingMessage sayHelloDestroy(final @Parameter("id") String id) {
    final GreetingMessage message = new GreetingMessage();

    message.setGreeting("Hello, destroyed " + id + "!");
    return message;
  }

  @Service(method = HttpMethod.GET, uriPattern = "/service")
  public GreetingMessage sayHelloGetService() {
    final GreetingMessage message = new GreetingMessage();

    message.setGreeting("Hello, gotten world!");
    return message;
  }

  @Service(method = HttpMethod.POST, uriPattern = "/service")
  public GreetingMessage sayHelloPostService(final ServiceMessage serviceMessage) {
    final GreetingMessage message = new GreetingMessage();

    message.setGreeting("Hello, posted " + serviceMessage.getValue() + "!");
    return message;
  }

  @Service(method = HttpMethod.PUT, uriPattern = "/service")
  public GreetingMessage sayHelloPutService(final ServiceMessage serviceMessage) {
    final GreetingMessage message = new GreetingMessage();

    message.setGreeting("Hello, put " + serviceMessage.getValue() + "!");
    return message;
  }

  @Service(method = HttpMethod.DELETE, uriPattern = "/service")
  public GreetingMessage sayHelloDeletedService() {
    final GreetingMessage message = new GreetingMessage();

    message.setGreeting("Hello, deleted world!");
    return message;
  }

  public static final class GreetingMessage {

    private String greeting;

    public final String getGreeting() {
      return this.greeting;
    }

    public final void setGreeting(final String greeting) {
      this.greeting = greeting;
    }

  }

  public static final class ServiceMessage {

    private String value;

    public final String getValue() {
      return this.value;
    }

    public final void setValue(final String value) {
      this.value = value;
    }

  }

}
