package com.nemesismate.piksel.trial.web.controller;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@WebFluxTest
public abstract class AbstractControllerTest {

    @Autowired
    protected WebTestClient testClient;

    protected WebTestClient.ResponseSpec responseSpec;


    protected void thenResponseBodyIsEmpty() {
        responseSpec.expectBody(Void.class);
    }

    protected void thenResponseCodeIsAccepted202() {
        responseSpec.expectStatus().isAccepted();
    }

    protected void thenResponseCodeIsUnprocessable422() {
        responseSpec.expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    protected void thenResponseCodeIsOk200() {
        responseSpec.expectStatus().isOk();
    }

    protected void thenResponseCodeIsNotFound404() {
        responseSpec.expectStatus().isNotFound();
    }

}
