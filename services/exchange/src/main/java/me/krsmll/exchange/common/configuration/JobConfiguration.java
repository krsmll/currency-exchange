package me.krsmll.exchange.common.configuration;

import java.util.List;
import me.krsmll.exchange.currency.job.CurrencyDataFetchingJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class JobConfiguration {
    @Bean
    public JobDetail currencyDataFetchingJobDetail() {
        return JobBuilder.newJob(CurrencyDataFetchingJob.class)
                .withIdentity("currencyDataFetchingJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger currencyDataFetchingJobScheduledTrigger(JobDetail currencyDataFetchingJob) {
        return TriggerBuilder.newTrigger()
                .forJob(currencyDataFetchingJob)
                .withIdentity("currencyDataFetchingJobTrigger")
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(0, 0))
                .build();
    }

    @Bean
    public Trigger currencyDataFetchingJobAppStartTrigger(JobDetail currencyDataFetchingJob) {
        return TriggerBuilder.newTrigger()
                .forJob(currencyDataFetchingJob)
                .withIdentity("currencyDataFetchingJobAppStartTrigger")
                .startNow()
                .build();
    }

    @Bean
    public Scheduler scheduler(List<Trigger> triggers, JobDetail job, SchedulerFactoryBean factory)
            throws SchedulerException {
        Scheduler scheduler = factory.getScheduler();
        for (Trigger trigger : triggers) {
            if (scheduler.checkExists(job.getKey())) {
                scheduler.deleteJob(job.getKey());
            }
            scheduler.scheduleJob(job, trigger);
        }
        scheduler.start();
        return scheduler;
    }
}
