package com.asd2.beerdistributiongame.network.util

import com.asd2.beerdistributiongame.context.Context.taskScheduler
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.SchedulingConfigurer
import org.springframework.scheduling.config.ScheduledTaskRegistrar

@Configuration
class SchedulingConfigurerConfiguration : SchedulingConfigurer {

    private val poolSize = 5

    override fun configureTasks(taskRegistrar: ScheduledTaskRegistrar) {
        taskScheduler.poolSize = poolSize
        taskScheduler.initialize()
        taskRegistrar.setTaskScheduler(taskScheduler)
    }
}