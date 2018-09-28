package com.nemesismate.piksel.trial.entity.vo;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Wither;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Wither
@Embeddable
@EqualsAndHashCode
@ToString
public class Viewings {

    @Column
    private int amount;

}
