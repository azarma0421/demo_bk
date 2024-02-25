package com.example.demo.scheduling;

import com.example.demo.service.ApiService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
class DailyApiCallerTest implements SchedulingConfigurer {
    @Autowired
    private ApiService apiService;

    private CountDownLatch latch = new CountDownLatch(1);

    @Test
    public void testScheduledTask() throws InterruptedException {
        latch = new CountDownLatch(1);
        TimeUnit.SECONDS.sleep(60);
        latch.await(60, TimeUnit.SECONDS);
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(
                apiService::callApi,
                triggerContext -> {
                    String cronExpression = "0/1 * * * * ?";
                    return new CronTrigger(cronExpression).nextExecutionTime(triggerContext).toInstant();
                }
        );
        taskRegistrar.afterPropertiesSet();
    }
}