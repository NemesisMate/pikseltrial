package com.nemesismate.piksel.trial.persistence;

import com.nemesismate.piksel.trial.entity.Studio;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface StudioRepository extends CrudRepository<Studio, UUID> {

    @Override
    Optional<Studio> findById(UUID uuid);
}
