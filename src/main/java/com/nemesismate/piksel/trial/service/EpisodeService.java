package com.nemesismate.piksel.trial.service;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface EpisodeService {

    Mono<Boolean> exists(UUID episodeId);

}
