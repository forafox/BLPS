package com.jellyone.blps.configuration

import com.jellyone.blps.service.quartz.UpdateRelevanceJob
import org.quartz.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
class QuartzConfig {

    @Bean
    fun updateRelevanceJobDetail(): JobDetail {
        return JobBuilder.newJob(UpdateRelevanceJob::class.java)
            .withIdentity("updateRelevanceJob")
            .storeDurably()
            .build()
    }

    @Bean
    fun updateRelevanceTrigger(): Trigger {
        return TriggerBuilder.newTrigger()
            .forJob(updateRelevanceJobDetail())
            .withIdentity("updateRelevanceTrigger")
            .withSchedule(
                SimpleScheduleBuilder.simpleSchedule()
//                    .withIntervalInSeconds(10)
                    .withIntervalInHours(12)
                    .repeatForever()
            )
            .build()
    }
}