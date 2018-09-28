package com.nemesismate.piksel.trial.web.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nemesismate.piksel.trial.util.serialization.NoDashUUIDDeserializer;
import com.nemesismate.piksel.trial.util.serialization.NoDashUUIDSerializer;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.UUID;


@Validated
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ViewingPayload {

    @NotNull
    @JsonDeserialize(using = NoDashUUIDDeserializer.class)
    @JsonSerialize(using = NoDashUUIDSerializer.class)
    private UUID episode;

    @NotNull
    @JsonDeserialize(using = NoDashUUIDDeserializer.class)
    @JsonSerialize(using = NoDashUUIDSerializer.class)
    private UUID customer;

}
