package com.nemesismate.piksel.trial.entity;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Wither;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Data
@ToString
@Builder
@Wither
public class Episode {

    @Id @GeneratedValue(generator = "uuidOrExisting")
    @GenericGenerator(name = "uuidOrExisting", strategy = "com.nemesismate.piksel.trial.util.serialization.UUIDGeneratorUseExisting")
    private UUID id;

    @NotNull
    private String name;

    @ManyToOne @NotNull
    private Studio rightsOwner;

}
