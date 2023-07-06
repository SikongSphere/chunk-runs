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
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.util.factory.Hints;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.sikongsphere.common.ConfigProvider;

import java.io.IOException;
import java.util.Date;

/**
 * @author Liang Chao
 * @date 2023/7/6 15:34
 */
@Slf4j
public class SimpleFeatureHandlerTest {

    private final String simpleFeatureTypeName = "ingestdemo";
    private final String simpleFeatureTypeField =
        "Author:String,dtg:Date,*geom:Point:srid=4326,description:String";
    private final SimpleFeatureUtils utils = new SimpleFeatureUtils();

    private SimpleFeature mkExampleData(SimpleFeatureType simpleFeatureType) {
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(simpleFeatureType);

        builder.set("Author", "sikongsphere");
        builder.set("dtg", new Date());
        builder.set("geom", "POINT (112.948 28.26)");
        builder.set("description", "This is a test for geomesa chunkruns");
        builder.featureUserData(Hints.USE_PROVIDED_FID, Boolean.TRUE);

        return builder.buildFeature("3");
    }

    @Test
    public void write() throws IOException {
        SimpleFeatureWriter writer = new SimpleFeatureWriter();
        DataStore dataStore = this.utils.mkConnection(ConfigProvider.TABLE_NAME);

        SimpleFeatureType simpleFeatureType = writer.mkSimpleFeatureType(
            this.simpleFeatureTypeName,
            this.simpleFeatureTypeField
        );
        SimpleFeature simpleFeature = this.mkExampleData(simpleFeatureType);

        writer.writeFeature(dataStore, simpleFeatureType, simpleFeature);
        log.info("Write Successfully!");

        writer.writeClose();
        dataStore.dispose();
    }

    @Test
    public void read() throws IOException {

        SimpleFeatureUtils utils = new SimpleFeatureUtils();
        DataStore dataStore = utils.mkConnection(ConfigProvider.TABLE_NAME);

        SimpleFeatureReader reader = new SimpleFeatureReader();
        Object[] array = reader.getFeatureType(dataStore, this.simpleFeatureTypeName).toArray();

        for (Object feature : array) {
            log.info(feature.toString());
        }

        dataStore.dispose();
    }
}
