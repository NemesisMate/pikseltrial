package com.nemesismate.piksel.trial.service.impl;

import com.nemesismate.piksel.trial.entity.Studio;
import com.nemesismate.piksel.trial.entity.vo.Viewings;
import com.nemesismate.piksel.trial.persistence.StudioRepository;
import com.nemesismate.piksel.trial.service.StudioService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class StudioServiceImpl implements StudioService {

    @Autowired
    private StudioRepository studioRepository;

    @Override
    public Mono<Optional<Studio>> get(@NonNull UUID uuid) {
        return Mono.just(uuid)
                    .publishOn(Schedulers.elastic())
                .map(studioRepository::findById)
                    .publishOn(Schedulers.parallel());
    }

    @Override
    public Flux<Studio> get(@NonNull Iterable<UUID> uuids) {
        return Mono.just(uuids)
                    .publishOn(Schedulers.elastic())
                .map(studioRepository::findAllById)
                    .publishOn(Schedulers.parallel())
                .flatMapMany(Flux::fromIterable);
    }

    @Override
    public Flux<Studio> getAll() {
        return Mono.just(studioRepository)
                    .publishOn(Schedulers.elastic())
                .map(StudioRepository::findAll)
                    .publishOn(Schedulers.parallel())
                .flatMapMany(Flux::fromIterable);
    }

    public Flux<Studio> saveAll(Iterable<Studio> studios) {
        return Mono.just(studios)
                    .publishOn(Schedulers.elastic())
                .map(studioRepository::saveAll)
                    .publishOn(Schedulers.parallel())
                .flatMapMany(Flux::fromIterable)
                ;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Iterable<Studio> addViewings(@NonNull Map<UUID, Viewings> viewingsPerCustomer) {
        return Mono.just(viewingsPerCustomer.keySet())
                    .map(studioRepository::findAllById)
                    .flatMapMany(Flux::fromIterable)
                    .doOnNext(studio -> this.addViewingsToStudio(studio, viewingsPerCustomer.get(studio.getId())))
                    .collectList()
                    .doOnNext(studioList -> log.debug("Updating studio views for: {}", studioList))
                    .map(studioRepository::saveAll)
                    .block()
                ;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void clearViewings() {
        Flux.fromIterable(studioRepository.findAll())
                .doOnNext(studio -> studio.setViewings(new Viewings(0)))
                .collectList()
                .map(studioRepository::saveAll)
                .subscribe();
    }


    void addViewingsToStudio(@NonNull Studio studio, @NonNull Viewings viewings) {
        studio.setViewings(new Viewings(studio.getViewings().getAmount() + viewings.getAmount()));
    }


}
