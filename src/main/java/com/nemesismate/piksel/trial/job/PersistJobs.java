package com.nemesismate.piksel.trial.job;

import com.nemesismate.piksel.trial.service.impl.ViewingServiceImpl;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collection;

@Component
public class PersistJobs {

    @AllArgsConstructor
    @ToString
    public static class PersistJobResult implements JobResult {
        private int persistedAmount;
    }

    @Autowired
    private ViewingServiceImpl viewingService;

    public Mono<JobResult> persistViews() {
        return viewingService.persistViews()
                .collectList()
                .map(Collection::size)
                .map(PersistJobResult::new);
    }

}
