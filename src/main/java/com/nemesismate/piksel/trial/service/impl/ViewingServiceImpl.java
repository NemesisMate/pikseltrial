package com.nemesismate.piksel.trial.service.impl;

import com.nemesismate.piksel.trial.entity.Studio;
import com.nemesismate.piksel.trial.entity.vo.Viewings;
import com.nemesismate.piksel.trial.service.StudioService;
import com.nemesismate.piksel.trial.service.ViewingService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ViewingServiceImpl implements ViewingService {

    final Map<UUID, AtomicInteger> views = new ConcurrentHashMap<>();

    @Autowired
    private StudioService studioService;


    @Override
    public Mono<Void> addView(@NonNull UUID studioId) {
        return Mono.fromRunnable(() -> addViews(studioId, 1));
    }

    void addViews(@NonNull UUID studioId, int amount) {
        log.debug("Adding {} views for: {}", amount, studioId);

        AtomicInteger viewings = views.get(studioId);

        if(Objects.isNull(viewings)) {
            viewings = new AtomicInteger();
            views.put(studioId, viewings);
        }

        viewings.addAndGet(amount);
    }

    public Flux<Studio> persistViews() {
        var viewingsBackup = new Object() { Map<UUID, Viewings> map; };

        return Flux.fromIterable(views.entrySet())
                        .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, entry -> new Viewings(views.remove(entry.getKey()).get())))
                        .doOnNext(mappedViews -> log.debug("Persisting views: {}", mappedViews))
                        .doOnNext(mappedViews -> viewingsBackup.map = mappedViews)
                            .publishOn(Schedulers.elastic())
                        .map(studioService::addViewings)
                            .publishOn(Schedulers.parallel())
                        .doOnError(throwable -> log.info("Backing up NOT-PERSISTED views: {}. Error occurred: ", viewingsBackup.map, throwable))
                        .doOnError(throwable -> viewingsBackup.map.forEach((uuid, viewings) -> addViews(uuid, viewings.getAmount())))
                        .flatMapMany(Flux::fromIterable)
                        ;
    }

    public Mono<Integer> getBufferedViews(UUID studioId) {
        return Mono.just(views).map((v) -> v.get(studioId)).map(AtomicInteger::get);
    }


    @Override
    public Mono<Void> resetBufferedViews() {
        return Mono.fromRunnable(views::clear);
    }

    @Override
    public Mono<Void> resetPersistedViews() {
        return Mono.just(studioService)
                    .publishOn(Schedulers.elastic())
                .doOnNext(StudioService::clearViewings)
                    .publishOn(Schedulers.parallel())
                .then();
    }

    @Override
    public Mono<Void> resetWholeViewingSystem() {
        return resetBufferedViews().then(resetPersistedViews());
    }
}
