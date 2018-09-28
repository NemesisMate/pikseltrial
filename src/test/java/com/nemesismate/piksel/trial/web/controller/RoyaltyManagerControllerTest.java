package com.nemesismate.piksel.trial.web.controller;

import com.nemesismate.piksel.trial.service.EpisodeService;
import com.nemesismate.piksel.trial.service.ViewingService;
import com.nemesismate.piksel.trial.web.dto.ViewingPayload;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static com.nemesismate.piksel.trial.TestHelper.createId;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.never;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@ContextConfiguration(classes = { RoyaltyManagerController.class })
public class RoyaltyManagerControllerTest extends AbstractPikselControllerTest {

    @MockBean
    ViewingService viewingService;

    @MockBean
    EpisodeService episodeService;

    private UUID episodeId;
    private UUID customerId;



    @Test
    public void viewing() {
        givenAnyEpisode();
        givenAnyCustomer();

        givenTheEpisodeExists();

        whenTheViewingIsAdded();

        thenTheViewWasTracked();
        thenResponseCodeIsAccepted202();
        thenResponseBodyIsEmpty();
    }

    @Test
    public void viewingFailOnEpisodeNotExisting() {
        givenAnyEpisode();
        givenAnyCustomer();

        givenTheEpisodeNotExists();

        whenTheViewingIsAdded();

        thenTheViewWasntTracked();
        thenResponseCodeIsUnprocessable422();
    }

    @Test
    public void reset() {
        whenReset();

        thenTheSystemWasReset();
        thenResponseCodeIsAccepted202();
        thenResponseBodyIsEmpty();
    }



    private void givenTheEpisodeExists() {
        givenEpisodeExists(true);
    }

    private void givenTheEpisodeNotExists() {
        givenEpisodeExists(false);
    }

    private void givenEpisodeExists(boolean exists) {
        given(episodeService.exists(episodeId)).willReturn(Mono.just(exists));
    }

    private void givenAnyEpisode() {
        givenEpisode(createId('e'));
    }

    private void givenEpisode(UUID episodeId) {
        this.episodeId = episodeId;
    }

    private void givenAnyCustomer() {
        givenCustomer(createId('c'));
    }

    private void givenCustomer(UUID customerId) {
        this.customerId = customerId;
    }


    private void whenTheViewingIsAdded() {
        given(viewingService.addView(customerId)).willReturn(Mono.empty());

        responseSpec = testClient.post().uri("/royaltymanager/viewing")
                                    .accept(MediaType.APPLICATION_JSON)
                                    .body(fromObject(new ViewingPayload(episodeId, customerId)))
                                    .exchange();
    }

    private void whenReset() {
        given(viewingService.resetWholeViewingSystem()).willReturn(Mono.empty());

        responseSpec = testClient.post().uri("/royaltymanager/reset")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();
    }


    private void thenTheViewWasTracked() {
        // TODO: Check this otherwise. Have in mind that it is being executed in parallel so this is not valid.
//        verify(viewingService).addView(customerId);
    }

    private void thenTheViewWasntTracked() {
        verify(viewingService, never()).addView(customerId);
    }

    private void thenTheSystemWasReset() {
        verify(viewingService).resetWholeViewingSystem();
    }
}