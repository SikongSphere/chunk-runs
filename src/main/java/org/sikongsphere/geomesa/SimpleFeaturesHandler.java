package org.sikongsphere.geomesa;

import org.geotools.data.DataStore;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.filter.identity.FeatureIdImpl;
import org.geotools.util.factory.Hints;
import org.locationtech.geomesa.utils.interop.SimpleFeatureTypes;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.sikongsphere.common.ConfigProvider;

import java.io.IOException;

/**
 * @author Liang Chao
 * @date 2023/7/5 11:46
 */
public class SimpleFeaturesHandler {

    public DataStore dataStore;
    private FeatureWriter<SimpleFeatureType, SimpleFeature> writer;

    public SimpleFeaturesHandler() throws IOException {
        this.dataStore  = ConfigProvider.mkConnection(ConfigProvider.TABLE_NAME);
    }

    public SimpleFeatureType setTable(String secondTableName, String tableStructure) {

        SimpleFeatureType sft = SimpleFeatureTypes.createType(secondTableName, tableStructure);
        sft.getUserData().put(SimpleFeatureTypes.DEFAULT_DATE_KEY, "dtg");

        return sft;
    }

    /**
     * write feature to geomesa-hbase datastore
     * @param simpleFeatureType Your simple feature type.
     * @param simpleFeature Your simple feature.
     * @throws IOException
     */
    public void writeFeature(SimpleFeatureType simpleFeatureType, SimpleFeature simpleFeature) throws IOException {

        this.writer = this.dataStore.getFeatureWriterAppend(simpleFeatureType.getTypeName(), Transaction.AUTO_COMMIT);
        SimpleFeature featureToWrite = this.writer.next();

        featureToWrite.setAttributes(simpleFeature.getAttributes());
        ((FeatureIdImpl) featureToWrite.getIdentifier()).setID(simpleFeature.getID());

        featureToWrite.getUserData().put(Hints.USE_PROVIDED_FID, Boolean.TRUE);
        featureToWrite.getUserData().putAll(simpleFeature.getUserData());

        this.writer.write();
    }

    /**
     * release resources after writing features.
     * @throws IOException
     */
    public void close() throws IOException {
        this.writer.close();
    }
}
