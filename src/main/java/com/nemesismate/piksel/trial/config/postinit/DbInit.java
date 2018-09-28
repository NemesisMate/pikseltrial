package com.nemesismate.piksel.trial.config.postinit;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.nemesismate.piksel.trial.util.serialization.NoDashUUIDDeserializer;
import com.nemesismate.piksel.trial.config.DbConfig.DbResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class DbInit {

    @Autowired
    private List<DbResource> dbResources;

    @PostConstruct
    public void initDb() {
        dbResources.forEach(this::populateDb);
    }

    public <T> void populateDb(DbResource<T> dbResource) {
        String resourceName = dbResource.resource.getFilename();

        log.info("Populating db from json: {}", resourceName);

        var loadedResources = loadResourcesFromFile(dbResource);
        var savedResources = saveResources(dbResource, loadedResources);

        log.debug("Populated db with: {}", savedResources);

        log.info("Db populated from json: {}", resourceName);
    }

    <T> Iterable<T> saveResources(DbResource<T> dbResource, Iterable<T> resources) {
        return dbResource.repository.saveAll(resources);
    }

    <T> Iterable<T> loadResourcesFromFile(DbResource<T> dbResource) {
        var mapper = setupMapper(dbResource);

        try {
            var collectionType = mapper.getTypeFactory().constructCollectionType(List.class, dbResource.entityClass);
            return mapper.readValue(dbResource.resource.getInputStream(), collectionType);
        } catch (IOException e) {
            log.error("Could not load db data from json: {}.", dbResource.resource.getFilename());
            throw new RuntimeException(e);
        }
    }

    ObjectMapper setupMapper(DbResource dbResource) {
        var mapper = new ObjectMapper();

        mapper.registerModule(new SimpleModule()
                .addDeserializer(UUID.class, new NoDashUUIDDeserializer())
        );

        if(dbResource.mapperModule != null) {
            mapper.registerModule(dbResource.mapperModule);
        }

        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

        return mapper;
    }
}
