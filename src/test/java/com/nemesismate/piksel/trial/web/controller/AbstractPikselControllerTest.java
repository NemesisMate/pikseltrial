package com.nemesismate.piksel.trial.web.controller;

import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = { RestExceptionHandler.class })
public abstract class AbstractPikselControllerTest extends AbstractControllerTest {

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

}
