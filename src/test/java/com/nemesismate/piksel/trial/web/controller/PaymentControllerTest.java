package com.nemesismate.piksel.trial.web.controller;

import com.nemesismate.piksel.trial.TestHelper;
import com.nemesismate.piksel.trial.entity.Studio;
import com.nemesismate.piksel.trial.service.StudioService;
import com.nemesismate.piksel.trial.web.dto.PaymentResponse;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static com.nemesismate.piksel.trial.TestHelper.anyFrom;
import static com.nemesismate.piksel.trial.TestHelper.paymentResponseFromStudio;
import static org.mockito.BDDMockito.given;

@ContextConfiguration(classes = { PaymentController.class })
public class PaymentControllerTest extends AbstractPikselControllerTest {

    @MockBean
    StudioService studioService;

    Studio studio;
    Collection<Studio> studios;

    @Test
    public void getAllWithExistingStudios() {
        givenAnyStudios();
        givenStudiosExist();

        whenGetAllPayments();

        thenResponseCodeIsOk200();
        thenResponseBodyIsPaymentForExistingStudios();
    }

    @Test
    public void getAllWithNotExistingStudios() {
        givenAnyStudios();
        givenNoStudiosExist();

        whenGetAllPayments();

        thenResponseCodeIsOk200();
        thenResponseBodyIsEmpty();
    }

    @Test
    public void getWithExistingStudio() {
        givenAnyStudios();
        givenStudiosExist();

        whenGetStudio();

        thenResponseCodeIsOk200();
        thenResponseBodyIsPaymentForRequestedStudio();
    }

    @Test
    public void getWithNotExistingStudio() {
        givenAnyStudios();
        givenNoStudiosExist();

        whenGetStudio();

        thenResponseCodeIsNotFound404();
    }

    private void givenAnyStudios() {
        givenStudios(TestHelper.createStudios());
    }

    private void givenStudios(Collection<Studio> studios) {
        this.studio = anyFrom(studios);
        this.studios = studios;
    }

    private void givenNoStudiosExist() {
        givenStudiosExist(Collections.emptySet());
    }

    private void givenStudiosExist() {
        givenStudiosExist(studios);
    }

    private void givenStudiosExist(Collection<Studio> studios) {
        given(studioService.getAll()).willReturn(Flux.fromIterable(studios));

        studios.forEach(studio ->
                given(studioService.get(studio.getId())).willReturn(Mono.just(Optional.of(studio)))
        );
        if(studios.isEmpty()) {
            given(studioService.get(studio.getId())).willReturn(Mono.just(Optional.empty()));
        }

    }


    private void whenGetAllPayments() {
        responseSpec = testClient.get().uri("/royaltymanager/payments")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();
    }

    private void whenGetStudio() {
        responseSpec = testClient.get().uri("/royaltymanager/payments/{rightsOwner}", studio.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange();
    }


    private void thenResponseBodyIsPaymentForExistingStudios() {
        PaymentResponse[] payments = studios.stream().map(TestHelper::paymentResponseFromStudio).toArray(PaymentResponse[]::new);

        responseSpec
                .expectBodyList(PaymentResponse.class)
                .contains(payments);
    }

    private void thenResponseBodyIsPaymentForRequestedStudio() {
        responseSpec
                .expectBody(PaymentResponse.class)
                .isEqualTo(paymentResponseFromStudio(studio));
    }
}