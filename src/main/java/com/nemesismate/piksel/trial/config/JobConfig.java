package com.nemesismate.piksel.trial.config;

import com.nemesismate.piksel.trial.job.JobResult;
import com.nemesismate.piksel.trial.job.PersistJobs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Slf4j
@Configuration
public class JobConfig {

    @Autowired
    private PersistJobs persistJobs;

    @Scheduled(fixedRate = 5000)
    public void persistViews() { triggerJob("PersistViewings", persistJobs, PersistJobs::persistViews); }


    private static <T> void triggerJob(String name, T jobs, Function<T, Mono<JobResult>> consumer) {
        consumer.apply(jobs)
                .doOnSubscribe(subscription -> log.info("Started job: {}", name))
                .doOnSuccess(jobResult -> log.info("Finished job: {}. Succeeded with result: {}", name, jobResult))
                .doOnError(throwable -> log.error("Finished job: {}. Errors occurred: {}", name, throwable))
                .subscribe();
    }
}
