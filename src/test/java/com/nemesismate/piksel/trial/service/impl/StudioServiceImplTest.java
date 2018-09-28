package com.nemesismate.piksel.trial.service.impl;

import com.nemesismate.piksel.trial.TestHelper;
import com.nemesismate.piksel.trial.entity.Studio;
import com.nemesismate.piksel.trial.entity.vo.Viewings;
import com.nemesismate.piksel.trial.persistence.StudioRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.test.StepVerifier;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.nemesismate.piksel.trial.TestHelper.anyFrom;
import static com.nemesismate.piksel.trial.TestHelper.idsFromStudios;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.willDoNothing;

@RunWith(MockitoJUnitRunner.class)
public class StudioServiceImplTest {


    @Mock
    StudioRepository studioRepository;

    @Spy
    @InjectMocks
    StudioServiceImpl studioService;


    private Viewings extraViewings;
    private Studio studio;
    private Collection<Studio> studios;

    private Viewings baseViewings;

    @Test
    public void getStudio() {
        givenAnyStudios();

        whenAnStudioIsRequested(thenTheStudioIsGot());
    }

    @Test
    public void getStudios() {
        givenAnyStudios();

        whenAnStudiosAreRequested(thenTheStudiosAreGot());
    }

    @Test
    public void getAllStudios() {
        givenAnyStudios();

        whenAllStudiosAreRequested(thenTheStudiosAreGot());
    }

    @Test
    public void addViewingsToStudios() {
        givenAnyStudios();
        givenAnyExtraViewings();

        whenTheExtraViewingsAreAddedToTheStudios();

        thenTheExtraViewingsAreSummedUpForAllStudios();
        thenTheStudiosAreUpdated();
    }

    @Test
    public void addViewingsToStudio() {
        givenAnyStudios();
        givenAnyExtraViewings();

        whenTheExtraViewingsIsAddedToTheStudio();

        thenTheExtraViewingsIsSummedUp();
    }

    @Test
    public void clearViewings() {
        givenAnyStudios();

        whenTheViewingsAreCleared();

        thenTheViewingsAreResetForAllStudios();
    }

    private void givenAnyStudios() {
        givenStudios(TestHelper.createStudios());
    }

    private void givenStudios(List<Studio> studios) {
        this.studio = anyFrom(studios);
        this.studios = studios;

        studios.forEach(studio ->
                given(studioRepository.findById(studio.getId())).willReturn(Optional.of(studio))
        );

        if(studios.isEmpty()) {
            given(studioRepository.findById(studio.getId())).willReturn(Optional.empty());
        }

        given(studioRepository.findAllById(idsFromStudios(studios))).willReturn(studios);
        given(studioRepository.findAll()).willReturn(studios);
        given(studioRepository.saveAll(studios)).willReturn(studios);
    }

    private void givenAnyExtraViewings() {
        givenExtraViewings(new Viewings(3));
    }

    private void givenExtraViewings(Viewings extraViewings) {
        this.extraViewings = extraViewings;
    }


    private void whenAllStudiosAreRequested(Consumer<List<Studio>> thenResult) {
        StepVerifier.create(studioService.getAll().collectList())
                .assertNext(thenResult)
                .verifyComplete();
    }

    private void whenAnStudiosAreRequested(Consumer<List<Studio>> thenResult) {
        StepVerifier.create(studioService.get(idsFromStudios(studios)).collectList())
                .assertNext(thenResult)
                .verifyComplete();
    }

    private void whenAnStudioIsRequested(Consumer<Optional<Studio>> thenResult) {
        StepVerifier.create(studioService.get(studio.getId()))
                .assertNext(thenResult)
                .verifyComplete();
    }

    private void whenTheExtraViewingsIsAddedToTheStudio() {
        baseViewings = studio.getViewings();

        studioService.addViewingsToStudio(studio, extraViewings);
    }

    private void whenTheExtraViewingsAreAddedToTheStudios() {
        Map<UUID, Viewings> viewings = studios.stream().collect(Collectors.toUnmodifiableMap(Studio::getId, studio -> extraViewings));

        willDoNothing().given(studioService).addViewingsToStudio(any(), any());

        studioService.addViewings(viewings);
    }

    private void whenTheViewingsAreCleared() {
        Objects.requireNonNull(studios);

        studioService.clearViewings();
    }



    private Consumer<Optional<Studio>> thenTheStudioIsGot() {
        return (optionalStudio) -> assertEquals(studio, optionalStudio.get());
    }

    private Consumer<List<Studio>> thenTheStudiosAreGot() {
        return (gotStudios) -> assertEquals(studios, gotStudios);
    }

    private void thenTheExtraViewingsIsSummedUp() {
        thenTheExtraViewingsIsSummedUp(baseViewings.getAmount() + extraViewings.getAmount());
    }

    private void thenTheExtraViewingsIsSummedUp(int expectedAmount) {
        assertEquals(expectedAmount, studio.getViewings().getAmount());
    }

    private void thenTheExtraViewingsAreSummedUpForAllStudios() {
        studios.forEach(studio ->
                verify(studioService).addViewingsToStudio(studio, extraViewings)
        );
    }

    private void thenTheViewingsAreResetForAllStudios() {
        studios.forEach(studio ->
                verify(studio).setViewings(eq(new Viewings(0)))
        );

        verify(studioRepository).saveAll(studios);
    }

    private void thenTheStudiosAreUpdated() {
        studios.forEach(studio ->
                verify(studioRepository).saveAll(studios)
        );
    }


}