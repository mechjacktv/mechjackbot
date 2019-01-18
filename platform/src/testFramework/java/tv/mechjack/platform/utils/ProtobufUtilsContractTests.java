package tv.mechjack.platform.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static tv.mechjack.testframework.TestFrameworkRule.ARBITRARY_COLLECTION_SIZE;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;

import tv.mechjack.testframework.TestFrameworkRule;
import tv.mechjack.util.ProtoMessage.TestKeyMessage;

public abstract class ProtobufUtilsContractTests {

  @Rule
  public final TestFrameworkRule testFrameworkRule = new TestFrameworkRule();

  protected abstract void installModules();

  protected abstract ProtobufUtils givenASubjectToTest();

  private Set<TestKeyMessage> givenASetOfTestKeyMessages() {
    final Set<TestKeyMessage> testKeyMessages = new HashSet<>();

    for (int i = 0; i < ARBITRARY_COLLECTION_SIZE; i++) {
      testKeyMessages.add(this.testFrameworkRule.getInstance(TestKeyMessage.class));
    }
    return testKeyMessages;
  }

  private Set<byte[]> givenASetOfRandomByteArrays() {
    final Set<byte[]> byteArraySet = new HashSet<>();

    for (int i = 0; i < ARBITRARY_COLLECTION_SIZE; i++) {
      byteArraySet.add(this.testFrameworkRule.getArbitraryByteArray());
    }
    return byteArraySet;
  }

  private Set<byte[]> givenASetOfTestKeyMessageByteArrays(final Set<TestKeyMessage> testKeyMessages) {
    final Set<byte[]> testKeyMessageBytesSet = new HashSet<>();

    for (final TestKeyMessage testKeyMessage : testKeyMessages) {
      testKeyMessageBytesSet.add(testKeyMessage.toByteArray());
    }
    return testKeyMessageBytesSet;
  }

  @Test
  public final void parseMessage_forMessageBytes_resultIsMessage() {
    this.installModules();
    final TestKeyMessage testKeyMessage = this.testFrameworkRule.getInstance(TestKeyMessage.class);
    final ProtobufUtils subjectUnderTest = this.givenASubjectToTest();

    final TestKeyMessage result = subjectUnderTest.parseMessage(TestKeyMessage.class, testKeyMessage.toByteArray());

    assertThat(result).isEqualTo(testKeyMessage);
  }

  @Test
  public final void parseMessage_badMessageBytes_throwsMessageParsingException() {
    this.installModules();
    final ProtobufUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.parseMessage(TestKeyMessage.class,
        this.testFrameworkRule.getArbitraryByteArray()));

    assertThat(thrown).isInstanceOf(MessageParsingException.class);
  }

  @Test
  public final void parseAllMessages_forMessageBytes_resultIsAllMessages() {
    this.installModules();
    final Set<TestKeyMessage> testKeyMessages = this.givenASetOfTestKeyMessages();
    final Set<byte[]> testKeyMessageBytesSet = this.givenASetOfTestKeyMessageByteArrays(testKeyMessages);
    final ProtobufUtils subjectUnderTest = this.givenASubjectToTest();

    final Collection<TestKeyMessage> result = subjectUnderTest.parseAllMessages(TestKeyMessage.class,
        testKeyMessageBytesSet);

    assertThat(result).containsExactlyInAnyOrderElementsOf(testKeyMessages);
  }

  @Test
  public final void parseAllMessages_badMessageBytes_throwsMessageParsingException() {
    this.installModules();
    final ProtobufUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.parseAllMessages(TestKeyMessage.class,
        this.givenASetOfRandomByteArrays()));

    assertThat(thrown).isInstanceOf(MessageParsingException.class);
  }

}
