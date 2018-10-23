package com.mechjacktv.scheduleservice;

import java.util.concurrent.TimeUnit;

public interface ScheduleService {

  void schedule(Runnable runnable, Integer period, TimeUnit unit);

  void stop();

}
