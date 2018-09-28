package com.nemesismate.piksel.trial.entity;

import com.nemesismate.piksel.trial.entity.vo.Viewings;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Wither;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@ToString
@Builder
@Wither
public class Studio {

    @Id @GeneratedValue(generator = "uuidOrExisting")
    @GenericGenerator(name = "uuidOrExisting", strategy = "com.nemesismate.piksel.trial.util.serialization.UUIDGeneratorUseExisting")
    private UUID id;

    @NotNull
    private String name;

    // Actually not used
//    @NotNull
//    private Currency currency = Currency.getInstance("GBP");

    @NotNull
    private BigDecimal payment;

    @Embedded
    private Viewings viewings = new Viewings();

    @OneToMany(cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<Episode> episodes;

}
