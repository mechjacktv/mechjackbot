package tv.mechjack.platform.utils.scheduleservice;

import java.util.concurrent.ScheduledExecutorService;

public class DefaultScheduleServiceUnitTests extends ScheduleServiceContractTests {

  @Override
  ScheduleService givenASubjectToTest(final ScheduledExecutorService scheduledExecutorService) {
    return new DefaultScheduleService(scheduledExecutorService);
  }

}
