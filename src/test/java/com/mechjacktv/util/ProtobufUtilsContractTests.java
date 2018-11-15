package com.mechjacktv.util;

import static com.mechjacktv.proto.util.UtilsMessage.TestMessage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.Collection;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

public abstract class ProtobufUtilsContractTests {

  private static final String TEST_VALUE = "TEST_VALUE";

  abstract ProtobufUtils givenASubjectToTest();

  @Test
  public final void parseMessage_forMessageBytes_returnsMessage() {
    final ProtobufUtils subjectUnderTest = this.givenASubjectToTest();
    final TestMessage testMessage = TestMessage.newBuilder().setValue(TEST_VALUE).build();

    final TestMessage result = subjectUnderTest.parseMessage(
        TestMessage.class, testMessage.toByteArray());

    assertThat(result).isEqualTo(testMessage);
  }

  @Test
  public final void parseMessage_badMessageBytes_throwsMessageParsingException() {
    final ProtobufUtils subjectUnderTest = this.givenASubjectToTest();
    final byte[] badMessageBytes = new byte[1024];
    new Random().nextBytes(badMessageBytes);

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.parseMessage(TestMessage.class,
        badMessageBytes));

    assertThat(thrown).isInstanceOf(MessageParsingException.class);
  }

  @Test
  public final void parseAllMessage_forMessageBytes_returnsMessage() {
    final ProtobufUtils subjectUnderTest = this.givenASubjectToTest();
    final TestMessage testMessage = TestMessage.newBuilder().setValue(TEST_VALUE).build();
    final Set<byte[]> messageBytesSet = Sets.newHashSet(testMessage.toByteArray());

    final Collection<TestMessage> result = subjectUnderTest.parseAllMessages(TestMessage.class, messageBytesSet);

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

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.parseAllMessages(TestMessage.class,
        messageBytesSet));

    assertThat(thrown).isInstanceOf(MessageParsingException.class);
  }

}
