package io.katharsis.spring.boot;

import com.fasterxml.jackson.databind.Module;
import io.katharsis.jackson.JsonApiModuleBuilder;
import io.katharsis.resource.registry.ResourceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfiguration {

    @Autowired
    private ResourceRegistry resourceRegistry;

    @Bean
    public Module parameterNamesModule() {
        JsonApiModuleBuilder jsonApiModuleBuilder = new JsonApiModuleBuilder();
        return jsonApiModuleBuilder.build(resourceRegistry);
    }
}
