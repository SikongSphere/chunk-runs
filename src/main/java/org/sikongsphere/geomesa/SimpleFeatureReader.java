/*
 * Copyright 2023 SikongSphere
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License version 2 as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
*/
package org.sikongsphere.geomesa;

import lombok.extern.slf4j.Slf4j;
import org.geotools.data.DataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author Liang Chao
 * @date 2023/7/6 15:42
 */
@Slf4j
public class SimpleFeatureReader {

    /**
     * get featureType from dataStore with
     */
    public SimpleFeatureCollection getFeatureType(DataStore dataStore, String simpleFeatureTypeName)
        throws IOException {

        String[] typeNames = dataStore.getTypeNames();

        if (typeNames.length > 0) {
            System.out.println(Arrays.toString(typeNames) + " Detected !");
            SimpleFeatureSource featureSource = dataStore.getFeatureSource(simpleFeatureTypeName);

            SimpleFeatureType schema = featureSource.getSchema();
            // 打印属性表结构
            log.info("Header: \n" + schema);

            return featureSource.getFeatures();
        } else {
            log.info("Nothing here!");
            return null;
        }
    }
}
