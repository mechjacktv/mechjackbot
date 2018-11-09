package com.mechjacktv.util;

public class DefaultProtobufUtilsUnitTests extends ProtobufUtilsContractTests {

  @Override
  ProtobufUtils givenASubjectToTest() {
    return new DefaultProtobufUtils(new DefaultExecutionUtils());
  }

}
