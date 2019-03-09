package tv.mechjack.mechjackbot.web;

import tv.mechjack.platform.webapp.api.HttpMethod;
import tv.mechjack.platform.webapp.api.resource.BaseController;
import tv.mechjack.platform.webapp.api.resource.RequestHandler;
import tv.mechjack.platform.webapp.api.resource.UriPattern;

public class TestController extends BaseController {

  public TestController() {
    super(UriPattern.of("/api/v1/test"));
  }

  @RequestHandler(method = HttpMethod.GET)
  public GreetingMessage sayHelloGet() {
    final GreetingMessage message = new GreetingMessage();

    message.setGreeting("Hello, GET!");
    return message;
  }

  @RequestHandler(method = HttpMethod.POST)
  public GreetingMessage sayHelloPost(
      final ServiceMessage serviceMessage) {
    final GreetingMessage message = new GreetingMessage();

    message.setGreeting("Hello, POST, " + serviceMessage.getValue() + "!");
    return message;
  }

  @RequestHandler(method = HttpMethod.PUT)
  public GreetingMessage sayHelloPut(
      final ServiceMessage serviceMessage) {
    final GreetingMessage message = new GreetingMessage();

    message.setGreeting("Hello, PUT, " + serviceMessage.getValue() + "!");
    return message;
  }

  @RequestHandler(method = HttpMethod.DELETE)
  public GreetingMessage sayHelloDeleted() {
    final GreetingMessage message = new GreetingMessage();

    message.setGreeting("Hello, DELETE!");
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
