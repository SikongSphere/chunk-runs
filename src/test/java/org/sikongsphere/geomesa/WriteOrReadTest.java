package org.sikongsphere.geomesa;

import lombok.extern.slf4j.Slf4j;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.util.factory.Hints;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

/**
 * @author Liang Chao
 * @date 2023/7/5 12:41
 */

@Slf4j
public class WriteOrReadTest {

    private SimpleFeaturesHandler handler;
    private SimpleFeatureType sft;

    @Before
    public void mkConnection() throws IOException {
        this.handler = new SimpleFeaturesHandler();
    }

    @Test
    public void putFeature() throws IOException {

        this.sft = this.handler.setTable(
                "ingestdemo",
                "Author:String,dtg:Date,*geom:Point:srid=4326,description:String"
        );

        this.handler.dataStore.createSchema(sft);

        /**
         * build simple features
         */
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(this.sft);

        builder.set("Author","sikongsphere");
        builder.set("dtg", new Date());
        builder.set("geom","POINT (112.988 28.26)");
        builder.set("description","This is a test for chunk-runs with geomesa-hbase");
        builder.featureUserData(Hints.USE_PROVIDED_FID,Boolean.TRUE);

        SimpleFeature feature = builder.buildFeature("1");

        /**
         * write to geomesa-hbase
         */
        this.handler.writeFeature(this.sft,feature);
        log.info("Data-ingestion Successfully Accomplished!");
    }

    @Test
    public void getFeatures() throws IOException {

        String[] typeNames = this.handler.dataStore.getTypeNames();
        if (typeNames.length > 0) {
            System.out.println(Arrays.toString(typeNames) + " Detected !");
            SimpleFeatureSource featureSource = this.handler.dataStore.getFeatureSource(typeNames[0]);

            SimpleFeatureType schema = featureSource.getSchema();
            log.info("Header: \n" + schema);
            SimpleFeatureCollection featureCollection = featureSource.getFeatures();
            Object[] collectionArray = featureCollection.toArray();

            for (int i = 0; i < 2; i++) {
                Object o = collectionArray[i];
                log.info(String.format("Data %d \t", i) + o);
            }
        } else {
            log.info("Nothing here!");
        }
    }


    @After
    public void clsConnect() throws IOException {

        this.handler.dataStore.dispose();
//        this.handler.close();
    }
}
