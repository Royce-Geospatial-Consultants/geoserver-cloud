package org.geoserver.cloud.catalog.modelmapper;

import org.geoserver.catalog.CoverageStoreInfo;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.StoreInfo;
import org.geoserver.catalog.WMSStoreInfo;
import org.geoserver.catalog.WMTSStoreInfo;
import org.geoserver.cloud.catalog.dto.CoverageStore;
import org.geoserver.cloud.catalog.dto.DataStore;
import org.geoserver.cloud.catalog.dto.Store;
import org.geoserver.cloud.catalog.dto.WMSStore;
import org.geoserver.cloud.catalog.dto.WMTSStore;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = SpringCatalogInfoMapperConfig.class)
public interface StoreMapper {

    default Store map(StoreInfo o) {
        if (o == null) return null;
        if (o instanceof DataStoreInfo) return map((DataStoreInfo) o);
        if (o instanceof CoverageStoreInfo) return map((CoverageStoreInfo) o);
        if (o instanceof WMSStoreInfo) return map((WMSStoreInfo) o);
        if (o instanceof WMTSStoreInfo) return map((WMTSStoreInfo) o);

        throw new IllegalArgumentException("Unknown StoreInfo type: " + o);
    }

    default StoreInfo map(Store o) {
        if (o == null) return null;
        if (o instanceof DataStore) return map((DataStore) o);
        if (o instanceof CoverageStore) return map((CoverageStore) o);
        if (o instanceof WMSStore) return map((WMSStore) o);
        if (o instanceof WMTSStore) return map((WMTSStore) o);

        throw new IllegalArgumentException("Unknown Store type: " + o);
    }

    @Mapping(target = "error", ignore = true)
    @Mapping(target = "catalog", ignore = true)
    DataStoreInfo map(DataStore o);

    DataStore map(DataStoreInfo o);

    @Mapping(target = "error", ignore = true)
    @Mapping(target = "catalog", ignore = true)
    CoverageStoreInfo map(CoverageStore o);

    CoverageStore map(CoverageStoreInfo o);

    @Mapping(target = "error", ignore = true)
    @Mapping(target = "catalog", ignore = true)
    WMSStoreInfo map(WMSStore o);

    WMSStore map(WMSStoreInfo o);

    @Mapping(target = "error", ignore = true)
    @Mapping(target = "catalog", ignore = true)
    WMTSStoreInfo map(WMTSStore o);

    WMTSStore map(WMTSStoreInfo o);
}
