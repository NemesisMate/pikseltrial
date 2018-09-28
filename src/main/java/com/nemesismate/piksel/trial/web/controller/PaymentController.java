package com.nemesismate.piksel.trial.web.controller;

import com.nemesismate.piksel.trial.entity.Studio;
import com.nemesismate.piksel.trial.service.StudioService;
import com.nemesismate.piksel.trial.util.UUIDUtils;
import com.nemesismate.piksel.trial.web.dto.PaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping("/royaltymanager/payments")
public class PaymentController {

    @Autowired
    private StudioService studioService;


    @GetMapping("")
    public Flux<PaymentResponse> getAll() {
        return studioService.getAll()
                .map(this::paymentResponseFromStudio);
    }

    @GetMapping("/{rightsOwner}")
    public Mono<PaymentResponse> get(@PathVariable String rightsOwner) {
        return Mono.just(rightsOwner)
                .log()
                .map(UUIDUtils::fromString)
                .flatMap(studioService::get)
                .map(optionalStudio -> studioFromOptional(optionalStudio, rightsOwner))
                .map(this::paymentResponseFromStudio);
    }


    private Studio studioFromOptional(Optional<Studio> optionalStudio, String rightsOwner) {
        return optionalStudio.orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Studio does NOT exist: " + rightsOwner ));
    }

    private PaymentResponse paymentResponseFromStudio(Studio studio) {
        return PaymentResponse.builder().studio(studio).build();
    }
}
