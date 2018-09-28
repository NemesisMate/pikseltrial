package com.nemesismate.piksel.trial.service.impl;

import com.nemesismate.piksel.trial.persistence.EpisodeRepository;
import com.nemesismate.piksel.trial.service.EpisodeService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Service
public class EpisodeServiceImpl implements EpisodeService {

    @Autowired
    private EpisodeRepository episodeRepository;

    @Override
    public Mono<Boolean> exists(@NonNull UUID episodeId) {
        return Mono.just(episodeId)
                    .publishOn(Schedulers.elastic())
                .map(episodeRepository::existsById)
                    .publishOn(Schedulers.parallel());
    }
}
