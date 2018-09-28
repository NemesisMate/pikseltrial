package com.nemesismate.piksel.trial.web.controller;

import com.nemesismate.piksel.trial.service.EpisodeService;
import com.nemesismate.piksel.trial.service.ViewingService;
import com.nemesismate.piksel.trial.web.dto.ViewingPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@RestController
@RequestMapping("/royaltymanager")
public class RoyaltyManagerController {

    @Autowired
    ViewingService viewingService;

    @Autowired
    EpisodeService episodeService;


    @PostMapping("/reset")
    public Mono<ResponseEntity<Void>> reset() {
        return viewingService.resetWholeViewingSystem()
                .thenReturn(ResponseEntity.accepted().build());
    }

    @PostMapping("/viewing")
    public Mono<ResponseEntity<Void>> viewing(@RequestBody Mono<ViewingPayload> viewingPayload) {
        return viewingPayload
                .filterWhen(this::validateEpisodeExists)
                .doOnNext(this::addView)
                .thenReturn(ResponseEntity.accepted().build());
    }

    private void addView(ViewingPayload viewingPayload) {
        Mono.just(viewingPayload)
                .map(ViewingPayload::getCustomer)
                .flatMap(viewingService::addView)
                .subscribeOn(Schedulers.parallel())
                .subscribe();
    }

    private Mono<Boolean> validateEpisodeExists(ViewingPayload viewingPayload) {
        return Mono.just(viewingPayload)
                .map(ViewingPayload::getEpisode)
                .flatMap(episodeService::exists)
                .doOnNext(exists -> {
                    if(!exists) {
                        throw new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY, "Episode does NOT exist: " + viewingPayload.getEpisode());
                    }
                });
    }
}
