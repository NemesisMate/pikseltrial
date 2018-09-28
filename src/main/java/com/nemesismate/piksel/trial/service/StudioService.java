package com.nemesismate.piksel.trial.service;

import com.nemesismate.piksel.trial.entity.Studio;
import com.nemesismate.piksel.trial.entity.vo.Viewings;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface StudioService {

    Mono<Optional<Studio>> get(UUID uuid);

    Flux<Studio> getAll();

    Flux<Studio> get(Iterable<UUID> uuids);


    Iterable<Studio> addViewings(Map<UUID, Viewings> viewingsPerCustomer);

    void clearViewings();
}
