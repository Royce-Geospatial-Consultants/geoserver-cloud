/*
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */
package org.geoserver.cloud.catalog.client.reactivefeign;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactivefeign.spring.config.EnableReactiveFeignClients;

@Configuration
@EnableReactiveFeignClients( //
    clients = { //
        ReactiveCatalogClient.class, //
        ReactiveConfigClient.class, //
        ReactiveResourceStoreClient.class
    }
)
public class ReactiveCatalogApiClientConfiguration {

    public @Bean ResourceStoreFallbackFactory resourceStoreFallbackFactory() {
        return new ResourceStoreFallbackFactory();
    }
}
