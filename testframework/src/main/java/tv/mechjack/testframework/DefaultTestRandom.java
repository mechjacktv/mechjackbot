package tv.mechjack.testframework;

public final class DefaultTestRandom implements TestRandom {

  private Long nextValue = 0L;

  DefaultTestRandom() {
    // making constructor package private
    super();
  }

  @Override
  public final Integer nextInteger() {
    return Integer.parseInt(this.nextValue.toString());
  }

  @Override
  public final void setNextValue(final Long nextValue) {
    this.nextValue = nextValue;
  }

  @Override
  public void reset() {
    this.nextValue = 0L;
  }

}
