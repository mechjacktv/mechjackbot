package tv.mechjack.testframework.fake.methodhandler;

import java.util.function.Function;

import tv.mechjack.testframework.fake.Invocation;
import tv.mechjack.testframework.fake.MethodInvocationHandler;

public class ValidatingMethodInvocationHandler extends MethodInvocationHandlerDecorator {

  private final Function<Invocation, Boolean> validator;
  private boolean valid;

  public ValidatingMethodInvocationHandler(final Function<Invocation, Boolean> validator) {
    this(validator, null);
  }

  public ValidatingMethodInvocationHandler(final Function<Invocation, Boolean> validator,
      final MethodInvocationHandler invocationHandler) {
    super(invocationHandler);
    this.validator = validator;
    this.valid = false;
  }

  @Override
  protected boolean execute(final Invocation invocation) {
    this.valid = this.validator.apply(invocation);
    return this.valid;
  }

  public final boolean isValid() {
    return this.valid;
  }

}
