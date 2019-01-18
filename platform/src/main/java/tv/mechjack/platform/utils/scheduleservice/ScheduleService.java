package tv.mechjack.platform.utils.scheduleservice;

import java.util.concurrent.TimeUnit;

public interface ScheduleService {

  void schedule(Runnable runnable, Integer period, TimeUnit unit);

  void schedule(Runnable runnable, Integer period, TimeUnit unit, Boolean delay);

  void stop();

}
