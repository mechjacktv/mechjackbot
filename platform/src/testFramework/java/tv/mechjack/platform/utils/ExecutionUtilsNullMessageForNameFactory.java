package tv.mechjack.platform.utils;

import javax.inject.Inject;

import tv.mechjack.testframework.NullMessageForNameFactory;

public class ExecutionUtilsNullMessageForNameFactory implements NullMessageForNameFactory {

  private final ExecutionUtils executionUtils;

  @Inject
  ExecutionUtilsNullMessageForNameFactory(final ExecutionUtils executionUtils) {
    this.executionUtils = executionUtils;
  }

  @Override
  public String create(final String name) {
    return this.executionUtils.nullMessageForName(name);
  }
}
