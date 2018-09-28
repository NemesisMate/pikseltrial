package com.nemesismate.piksel.trial.service.impl;

import com.nemesismate.piksel.trial.entity.Studio;
import com.nemesismate.piksel.trial.entity.vo.Viewings;
import com.nemesismate.piksel.trial.service.StudioService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.nemesismate.piksel.trial.TestHelper.anyFrom;
import static com.nemesismate.piksel.trial.TestHelper.compose;
import static com.nemesismate.piksel.trial.TestHelper.createId;
import static com.nemesismate.piksel.trial.TestHelper.studiosFromViewings;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.willDoNothing;

@RunWith(MockitoJUnitRunner.class)
public class ViewingServiceImplTest {

    @Mock
    StudioService studioService;

    @Spy
    @InjectMocks
    ViewingServiceImpl viewingService;


    private UUID studioId;
    private Collection<UUID> studioIds;
    private Integer views = 0;
    private Integer bufferedViews = 0;

    private Map<UUID, Viewings> viewings;



    @Test
    public void getBufferedViews() {
        givenAnyStudioIds();
        givenAnyBufferedViews();

        whenGettingTheBufferedViews(thenTheBufferedViewsAreGot());
    }

    @Test
    public void addView() {
        givenAnyStudioIds();

        whenAddViewForStudio(thenOneViewIsAddedForTheStudioId());
    }

    @Test
    public void addViews() {
        givenAnyStudioIds();
        givenAnyViews();

        whenAddViewForStudio(thenTheViewsAreAddedForTheStudioId());
    }

    @Test
    public void persistViews() {
        givenAnyStudioIds();
        givenAnyBufferedViews();

        whenPersistingViews(compose(
                thenTheViewsAreAddedToTheStudioService(),
                thenTheStudiosAreReturned()
        ));
    }

    @Ignore("TBD")
    @Test
    public void persistViewsFails() {
        //TODO: Rollback test for when persist fails
    }


    private void givenAnyStudioIds() {
        givenStudioIds(Arrays.asList(createId('a'), createId('b'), createId('c')));
    }

    private void givenStudioIds(Collection<UUID> studioIds) {
        this.studioId = anyFrom(studioIds);
        this.studioIds = studioIds;
    }


    private void givenAnyBufferedViews() {
        givenBufferedViews(7);
    }

    private void givenBufferedViews(int amount) {
        studioIds.forEach(id -> viewingService.addViews(id, amount));
        viewings = studioIds.stream().collect(Collectors.toMap(id -> id, id -> new Viewings(amount)));

        bufferedViews = amount;
    }

    private void givenAnyViews() {
        givenViews(7);
    }

    private void givenViews(int amount) {
        views = amount;
    }


    private void whenAddViewForStudio(Consumer<Void> thenResult) {
        willDoNothing().given(viewingService).addViews(studioId, 1);

        viewingService.addView(studioId).subscribe(thenResult);
    }

    private void whenGettingTheBufferedViews(Consumer<Integer> thenResult) {
        viewingService.getBufferedViews(studioId).subscribe(thenResult);
    }

    private void whenPersistingViews(Consumer<Collection<Studio>> thenAssert) {
        Iterable<Studio> studios = studiosFromViewings(viewings);
        given(studioService.addViewings(viewings)).willReturn(studios);

        StepVerifier.create(viewingService.persistViews().collectList())
                .assertNext(thenAssert)
                .verifyComplete();
    }


    private Consumer<Void> thenOneViewIsAddedForTheStudioId() {
        return v -> verify(viewingService).addView(studioId);
    }

    private Consumer<Integer> thenTheBufferedViewsAreGot() {
        return integer -> assertEquals(bufferedViews, integer);
    }

    private Consumer<Void> thenTheViewsAreAddedForTheStudioId() {
        return v -> assertEquals(Integer.valueOf(views + bufferedViews), viewingService.getBufferedViews(studioId).block());
    }

    private Consumer<Collection<Studio>> thenTheViewsAreAddedToTheStudioService() {
        return c -> verify(studioService).addViewings(viewings);
    }

    private Consumer<Collection<Studio>> thenTheStudiosAreReturned() {
        return collection -> assertEquals(studiosFromViewings(viewings), collection);
    }
}