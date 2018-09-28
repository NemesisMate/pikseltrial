package com.nemesismate.piksel.trial.service.impl;

import com.nemesismate.piksel.trial.TestHelper;
import com.nemesismate.piksel.trial.entity.Episode;
import com.nemesismate.piksel.trial.persistence.EpisodeRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.test.StepVerifier;

import java.util.function.Consumer;

import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class EpisodeServiceImplTest {

    @Mock
    EpisodeRepository episodeRepository;

    @InjectMocks
    EpisodeServiceImpl episodeService;


    private Episode episode;


    @Test
    public void episodeExists() {
        givenAnyEpisode();

        whenTheEpisodeExistanceIsChecked(thenTheEpisodeExists());
    }

    @Test
    public void episodeDoesNotExist() {
        givenAnyEpisode();
        givenTheEpisodeIsNotSaved();

        whenTheEpisodeExistanceIsChecked(thenTheEpisodeDoesNotExist());
    }


    private void givenTheEpisodeIsNotSaved() {
        given(episodeRepository.existsById(episode.getId())).willReturn(false);
    }

    private void givenAnyEpisode() {
        givenEpisode(TestHelper.createEpisode());
    }

    private void givenEpisode(Episode episode) {
        this.episode = episode;

        given(episodeRepository.existsById(episode.getId())).willReturn(true);
    }


    private void whenTheEpisodeExistanceIsChecked(Consumer<Boolean> thenResult) {
        StepVerifier.create(episodeService.exists(episode.getId()))
                .consumeNextWith(thenResult)
                .verifyComplete();
    }


    private Consumer<Boolean> thenTheEpisodeExists() {
        return Assert::assertTrue;
    }

    private Consumer<Boolean> thenTheEpisodeDoesNotExist() {
        return Assert::assertFalse;
    }



}