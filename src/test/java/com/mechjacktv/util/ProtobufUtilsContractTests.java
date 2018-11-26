package com.mechjacktv.util;

import java.util.Collection;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;
import com.mechjacktv.proto.util.UtilsMessage.TestKeyMessage;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public abstract class ProtobufUtilsContractTests {

  private static final String TEST_VALUE = "TEST_VALUE";

  abstract ProtobufUtils givenASubjectToTest();

  @Test
  public final void parseMessage_forMessageBytes_returnsMessage() {
    final ProtobufUtils subjectUnderTest = this.givenASubjectToTest();
    final TestKeyMessage testMessage = TestKeyMessage.newBuilder().setValue(TEST_VALUE).build();

    final TestKeyMessage result = subjectUnderTest.parseMessage(
        TestKeyMessage.class, testMessage.toByteArray());

    assertThat(result).isEqualTo(testMessage);
  }

  @Test
  public final void parseMessage_badMessageBytes_throwsMessageParsingException() {
    final ProtobufUtils subjectUnderTest = this.givenASubjectToTest();
    final byte[] badMessageBytes = new byte[1024];
    new Random().nextBytes(badMessageBytes);

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.parseMessage(TestKeyMessage.class,
        badMessageBytes));

    assertThat(thrown).isInstanceOf(MessageParsingException.class);
  }

  @Test
  public final void parseAllMessage_forMessageBytes_returnsMessage() {
    final ProtobufUtils subjectUnderTest = this.givenASubjectToTest();
    final TestKeyMessage testMessage = TestKeyMessage.newBuilder().setValue(TEST_VALUE).build();
    final Set<byte[]> messageBytesSet = Sets.newHashSet(testMessage.toByteArray());

    final Collection<TestKeyMessage> result = subjectUnderTest.parseAllMessages(TestKeyMessage.class, messageBytesSet);

    final SoftAssertions softly = new SoftAssertions();
    softly.assertThat(result).hasSize(1);
    softly.assertThat(result).contains(testMessage);
    softly.assertAll();
  }

  @Test
  public final void parseAllMessage_badMessageBytes_throwsMessageParsingException() {
    final ProtobufUtils subjectUnderTest = this.givenASubjectToTest();
    final byte[] badMessageBytes = new byte[1024];
    final Set<byte[]> messageBytesSet = Sets.newHashSet(badMessageBytes);
    new Random().nextBytes(badMessageBytes);

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.parseAllMessages(TestKeyMessage.class,
        messageBytesSet));

    assertThat(thrown).isInstanceOf(MessageParsingException.class);
  }

}
