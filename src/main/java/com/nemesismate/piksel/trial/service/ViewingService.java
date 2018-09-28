package com.nemesismate.piksel.trial.service;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ViewingService {

    Mono<Void> addView(UUID customerId);

    Mono<Void> resetBufferedViews();

    Mono<Void> resetPersistedViews();

    Mono<Void> resetWholeViewingSystem();



}
