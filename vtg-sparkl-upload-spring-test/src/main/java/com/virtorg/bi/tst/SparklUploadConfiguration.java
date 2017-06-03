package com.virtorg.bi.tst;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import com.virtorg.bi.tst.components.UploadFileREST;

@Configuration
@EnableScheduling
public class SparklUploadConfiguration {

	@Bean(name = "taskScheduler")
	public TaskScheduler taskSchedular() {
		ThreadPoolTaskScheduler taskSchedular = new ThreadPoolTaskScheduler();
		taskSchedular.setPoolSize(5);
		return (TaskScheduler) taskSchedular;
	}
	
}
