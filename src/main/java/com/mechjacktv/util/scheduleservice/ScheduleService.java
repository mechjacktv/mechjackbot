package com.mechjacktv.util.scheduleservice;

import java.util.concurrent.TimeUnit;

public interface ScheduleService {

  void schedule(Runnable runnable, Integer period, TimeUnit unit);

  void schedule(Runnable runnable, Integer period, TimeUnit unit, boolean delay);

  void stop();

}
