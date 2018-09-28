package com.nemesismate.piksel.trial.persistence;

import com.nemesismate.piksel.trial.entity.Episode;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface EpisodeRepository extends CrudRepository<Episode, UUID> {
}
