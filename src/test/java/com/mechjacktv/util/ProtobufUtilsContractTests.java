package com.mechjacktv.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.mechjacktv.proto.util.UtilsMessage.TestKeyMessage;
import com.mechjacktv.testframework.ArbitraryDataGenerator;

public abstract class ProtobufUtilsContractTests {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();

  abstract ProtobufUtils givenASubjectToTest();

  private Set<TestKeyMessage> givenASetOfTestKeyMessages() {
    final Set<TestKeyMessage> testKeyMessages = new HashSet<>();

    for (int i = 0; i < 10; i++) {
      testKeyMessages.add(this.arbitraryDataGenerator.getTestKeyMessage());
    }
    return testKeyMessages;
  }

  private Set<byte[]> givenASetOfRandomByteArrays() {
    final Set<byte[]> byteArraySet = new HashSet<>();

    for (int i = 0; i < 10; i++) {
      byteArraySet.add(this.arbitraryDataGenerator.getByteArray());
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
  public final void parseMessage_forMessageBytes_returnsMessage() {
    final TestKeyMessage testKeyMessage = this.arbitraryDataGenerator.getTestKeyMessage();
    final ProtobufUtils subjectUnderTest = this.givenASubjectToTest();

    final TestKeyMessage result = subjectUnderTest.parseMessage(TestKeyMessage.class, testKeyMessage.toByteArray());

    assertThat(result).isEqualTo(testKeyMessage);
  }

  @Test
  public final void parseMessage_badMessageBytes_throwsMessageParsingException() {
    final ProtobufUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.parseMessage(TestKeyMessage.class,
        this.arbitraryDataGenerator.getByteArray()));

    assertThat(thrown).isInstanceOf(MessageParsingException.class);
  }

  @Test
  public final void parseAllMessage_forMessageBytes_returnsMessage() {
    final Set<TestKeyMessage> testKeyMessages = this.givenASetOfTestKeyMessages();
    final Set<byte[]> testKeyMessageBytesSet = this.givenASetOfTestKeyMessageByteArrays(testKeyMessages);
    final ProtobufUtils subjectUnderTest = this.givenASubjectToTest();

    final Collection<TestKeyMessage> result = subjectUnderTest.parseAllMessages(TestKeyMessage.class,
        testKeyMessageBytesSet);

    assertThat(result).containsExactlyInAnyOrderElementsOf(testKeyMessages);
  }

  @Test
  public final void parseAllMessage_badMessageBytes_throwsMessageParsingException() {
    final ProtobufUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.parseAllMessages(TestKeyMessage.class,
        this.givenASetOfRandomByteArrays()));

    assertThat(thrown).isInstanceOf(MessageParsingException.class);
  }

}
