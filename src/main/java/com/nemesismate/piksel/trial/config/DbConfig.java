package com.nemesismate.piksel.trial.config;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.nemesismate.piksel.trial.entity.Episode;
import com.nemesismate.piksel.trial.entity.Studio;
import com.nemesismate.piksel.trial.persistence.EpisodeRepository;
import com.nemesismate.piksel.trial.persistence.StudioRepository;
import com.nemesismate.piksel.trial.util.UUIDUtils;
import lombok.Builder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.CrudRepository;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
public class DbConfig {

    @Builder
    public static class DbResource<T> {
        public @NonNull Resource resource;
        public @NonNull Class<T> entityClass;
        public @NonNull CrudRepository<T, ?> repository;
        public Module mapperModule;
    }

    private class StudioByUUIDDeserializer extends FromStringDeserializer<Studio> {

        private StudioByUUIDDeserializer() {
            super(Studio.class);
        }

        @Override
        protected Studio _deserialize(String value, DeserializationContext ctxt) {
            return studioRepository.findById(UUIDUtils.fromStringNoDashes(value)).get();
        }
    }

    @Value("${trial.db.studios}")
    private Resource studiosFile;

    @Value("${trial.db.episodes}")
    private Resource episodesFile;

    @Autowired
    private StudioRepository studioRepository;

    @Autowired
    private EpisodeRepository episodeRepository;

    @Bean
    List<DbResource> dbResources() {
        return Arrays.asList(
                DbResource.<Studio>builder()
                        .resource(studiosFile)
                        .entityClass(Studio.class)
                        .repository(studioRepository)
                        .build(),
                DbResource.<Episode>builder()
                        .resource(episodesFile)
                        .entityClass(Episode.class)
                        .repository(episodeRepository)
                        .mapperModule(new SimpleModule().addDeserializer(Studio.class, new StudioByUUIDDeserializer()))
                        .build()
        );
    }
}
