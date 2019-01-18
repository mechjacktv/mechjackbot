package tv.mechjack.testframework;

public interface TestRandom {

  Integer nextInteger();

  void setNextValue(Long nextValue);

  void reset();

}
