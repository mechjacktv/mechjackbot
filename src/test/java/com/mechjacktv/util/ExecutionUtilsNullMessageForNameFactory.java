package com.mechjacktv.util;

import javax.inject.Inject;

import com.mechjacktv.testframework.NullMessageForNameFactory;

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
