package com.nemesismate.piksel.trial.web.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nemesismate.piksel.trial.entity.Studio;
import com.nemesismate.piksel.trial.util.serialization.NoDashUUIDDeserializer;
import com.nemesismate.piksel.trial.util.serialization.NoDashUUIDSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
@EqualsAndHashCode
public class PaymentResponse {

    @NotNull
    @JsonSerialize(using = NoDashUUIDSerializer.class)
    @JsonDeserialize(using = NoDashUUIDDeserializer.class)
    private UUID rightsownerId;

    @NotNull
    private String rightsowner;

    @NotNull
    private Float royalty;

    @NotNull
    private Integer viewings;

    public static class PaymentResponseBuilder {

        public PaymentResponseBuilder studio(Studio studio) {
            this.rightsownerId = studio.getId();
            this.rightsowner = studio.getName();
            this.royalty = studio.getViewings().getAmount() * studio.getPayment().floatValue();
            this.viewings = studio.getViewings().getAmount();

            return this;
        }
    }

}
