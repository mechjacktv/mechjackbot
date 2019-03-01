package tv.mechjack.mechjackbot.web;

import tv.mechjack.platform.web.services.Controller;
import tv.mechjack.platform.web.services.HttpMethod;
import tv.mechjack.platform.web.services.Service;

public class TestController implements Controller {

  @Service(method = HttpMethod.GET, path = "/api/v1/test")
  public GreetingMessage sayHelloGet() {
    final GreetingMessage message = new GreetingMessage();

    message.setGreeting("Hello, world!");
    return message;
  }

  @Service(method = HttpMethod.POST, path = "/api/v1/test")
  public GreetingMessage sayHelloPost(final PostMessage postMessage) {
    final GreetingMessage message = new GreetingMessage();

    message.setGreeting("Hello, " + postMessage.getValue() + "!");
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

  public static final class PostMessage {

    private String value;

    public final String getValue() {
      return this.value;
    }

    public final void setValue(final String value) {
      this.value = value;
    }

  }

}
