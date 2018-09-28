package com.nemesismate.piksel.trial;

import com.nemesismate.piksel.trial.entity.Episode;
import com.nemesismate.piksel.trial.entity.Studio;
import com.nemesismate.piksel.trial.entity.vo.Viewings;
import com.nemesismate.piksel.trial.web.dto.PaymentResponse;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.mockito.Mockito.spy;

public final class TestHelper {

    private TestHelper() { }

    public static <T> T anyFrom(Collection<T> collection) {
        return CollectionUtils.isEmpty(collection) ? null : collection.iterator().next();
    }

    public static <T> Consumer<T> compose(Consumer<T>... consumers) {
        Objects.requireNonNull(consumers);

        return Arrays.stream(consumers).reduce(Consumer::andThen).get();
    }

    public static Iterable<UUID> idsFromStudios(Collection<Studio> studioList) {
        return studioList.stream().map(Studio::getId).collect(Collectors.toUnmodifiableSet());
    }

    public static Iterable<Studio> studiosFromViewings(Map<UUID, Viewings> viewings) {
        return viewings.entrySet().stream().map(entry ->
                createStudio().withId(entry.getKey()).withViewings(entry.getValue())
        ).collect(Collectors.toList());
    }

    public static List<Studio> createStudios() {
        Studio baseStudio = createStudio();

        return Arrays.asList(
                spy(baseStudio.withViewings(new Viewings(1)).withId(createId('a'))),
                spy(baseStudio.withViewings(new Viewings(4)).withId(createId('b'))),
                spy(baseStudio.withViewings(new Viewings(3)).withId(createId('c')))
        );
    }

    public static Studio createStudio() {
        return spy(Studio.builder()
                .id(createId('0'))
                .name("CommonStudio")
                .payment(BigDecimal.valueOf(7.3))
                .viewings(new Viewings(3))
                .build());
    }

    public static Episode createEpisode() {
        Episode episode = Episode.builder()
                            .id(createId('1'))
                            .name("CommonEpisode")
                            .build();

        episode.withRightsOwner(createStudio().withEpisodes(Collections.singletonList(episode)));

        return spy(episode);
    }

    public static UUID createId(char character) {
        String lowerLetter = String.valueOf(character).toLowerCase();

        if(!lowerLetter.matches("^[0-9a-f]$")) {
            throw new IllegalArgumentException("The id character must be hexadecimal. Given: " + lowerLetter);
        }

        return UUID.fromString("xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx".replace('x', character));
    }

    public static PaymentResponse paymentResponseFromStudio(Studio studio) {
        return PaymentResponse.builder().studio(studio).build();
    }

}
