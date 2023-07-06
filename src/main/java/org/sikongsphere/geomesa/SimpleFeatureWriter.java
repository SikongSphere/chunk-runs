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

import org.geotools.data.DataStore;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.filter.identity.FeatureIdImpl;
import org.geotools.util.factory.Hints;
import org.locationtech.geomesa.utils.interop.SimpleFeatureTypes;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.IOException;

/**
 * @author Liang Chao
 * @date 2023/7/6 15:31
 */
public class SimpleFeatureWriter {

    private FeatureWriter<SimpleFeatureType, SimpleFeature> writer;

    /**
     * used to make simpleFeatureType with specified Name and Fields.
     * @param simpleFeatureTypeName name of sft.
     * @param simpleFeatureTypeField fields of sft.
     * @return simpleFeatureType
     */
    public SimpleFeatureType mkSimpleFeatureType(
        String simpleFeatureTypeName,
        String simpleFeatureTypeField
    ) {
        SimpleFeatureType sft = SimpleFeatureTypes.createType(
            simpleFeatureTypeName,
            simpleFeatureTypeField
        );
        sft.getUserData().put(SimpleFeatureTypes.DEFAULT_DATE_KEY, "dtg");

        return sft;
    }

    /**
     * @param dataStore datastore
     * @param sft simpleFeatureType
     * @param feature simpleFeature
     */
    public void writeFeature(DataStore dataStore, SimpleFeatureType sft, SimpleFeature feature)
        throws IOException {
        this.writer = dataStore.getFeatureWriterAppend(sft.getTypeName(), Transaction.AUTO_COMMIT);

        SimpleFeature toWrite = this.writer.next();
        toWrite.setAttributes(feature.getAttributes());
        ((FeatureIdImpl) toWrite.getIdentifier()).setID(feature.getID());

        toWrite.getUserData().put(Hints.USE_PROVIDED_FID, Boolean.TRUE);
        toWrite.getUserData().putAll(feature.getUserData());

        this.writer.write();
    }

    /**
     * release resources after writing
     * @throws IOException .
     */
    public void writeClose() throws IOException {
        this.writer.close();
    }
}
