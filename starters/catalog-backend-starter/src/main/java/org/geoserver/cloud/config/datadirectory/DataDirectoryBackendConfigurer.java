/*
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */
package org.geoserver.cloud.config.datadirectory;

import java.io.File;
import java.nio.file.Path;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.geoserver.catalog.plugin.DefaultMemoryCatalogFacade;
import org.geoserver.cloud.autoconfigure.bus.ConditionalOnGeoServerRemoteEventsEnabled;
import org.geoserver.cloud.config.catalog.GeoServerBackendConfigurer;
import org.geoserver.cloud.config.catalog.GeoServerBackendProperties;
import org.geoserver.config.GeoServerLoader;
import org.geoserver.config.plugin.RepositoryGeoServerFacade;
import org.geoserver.platform.GeoServerResourceLoader;
import org.geoserver.platform.resource.FileLockProvider;
import org.geoserver.platform.resource.ResourceStore;
import org.geoserver.wcs.WCSXStreamLoader;
import org.geoserver.wfs.WFSXStreamLoader;
import org.geoserver.wms.WMSXStreamLoader;
import org.geoserver.wps.WPSXStreamLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/** */
@Configuration(proxyBeanMethods = true)
@Slf4j
public class DataDirectoryBackendConfigurer implements GeoServerBackendConfigurer {

    private @Autowired @Getter ApplicationContext context;
    private @Autowired GeoServerBackendProperties configProperties;

    public @PostConstruct void log() {
        log.info("Loading geoserver config backend with {}", getClass().getSimpleName());
    }

    @ConditionalOnGeoServerRemoteEventsEnabled
    public @Bean DataDirectoryRemoteEventProcessor dataDirectoryRemoteEventProcessor() {
        return new DataDirectoryRemoteEventProcessor();
    }

    public @Override @Bean DefaultMemoryCatalogFacade catalogFacade() {
        return new org.geoserver.catalog.plugin.DefaultMemoryCatalogFacade();
    }

    public @Override @Bean RepositoryGeoServerFacade geoserverFacade() {
        return new org.geoserver.config.plugin.RepositoryGeoServerFacadeImpl();
    }

    @DependsOn({"extensions", "wmsLoader", "wfsLoader", "wcsLoader", "wpsServiceLoader"})
    public @Override @Bean GeoServerLoader geoServerLoaderImpl() {
        GeoServerResourceLoader resourceLoader = resourceLoader();
        return new DataDirectoryGeoServerLoader(resourceLoader);
    }

    public @Override @Bean GeoServerResourceLoader resourceLoader() {
        ResourceStore resourceStore = resourceStoreImpl();
        GeoServerResourceLoader resourceLoader = new GeoServerResourceLoader(resourceStore);
        Path location = configProperties.getDataDirectory().getLocation();
        log.debug("geoserver.backend.data-directory.location:" + location);
        File dataDirectory = location.toFile();
        resourceLoader.setBaseDirectory(dataDirectory);
        return resourceLoader;
    }

    public @Override @Bean ResourceStore resourceStoreImpl() {
        Path path = configProperties.getDataDirectory().getLocation();
        File dataDirectory = path.toFile();
        NoServletContextDataDirectoryResourceStore store =
                new NoServletContextDataDirectoryResourceStore(dataDirectory);
        store.setLockProvider(new FileLockProvider(dataDirectory));
        return store;
    }

    /**
     * Provide {@code wmsLoader} if not loaded from {@code
     * gs-wms-<version>.jar!/applicationContext.xml#wmsLoader}
     */
    public @Bean WMSXStreamLoader wmsLoader() {
        return new WMSXStreamLoader(resourceLoader());
    }

    /**
     * Provide {@code wfsLoader} if not loaded from {@code
     * gs-wfs-<version>.jar!/applicationContext.xml#wfsLoader}
     */
    public @Bean WFSXStreamLoader wfsLoader() {
        return new WFSXStreamLoader(resourceLoader());
    }

    /**
     * Provide {@code wcsLoader} if not loaded from {@code
     * gs-wcs-<version>.jar!/applicationContext.xml#wcsLoader}
     */
    public @Bean WCSXStreamLoader wcsLoader() {
        return new WCSXStreamLoader(resourceLoader());
    }

    /**
     * Provide {@code wcsLoader} if not loaded from {@code
     * gs-wps-<version>.jar!/applicationContext.xml#wpsServiceLoader}
     */
    public @Bean WPSXStreamLoader wpsServiceLoader() {
        return new WPSXStreamLoader(resourceLoader());
    }
}
