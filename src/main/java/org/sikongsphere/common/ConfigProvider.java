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
package org.sikongsphere.common;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Liang Chao
 * @date 2023/7/5 11:46
 */

public class ConfigProvider {

    /**
     * strings constants.
     */
    public static final String HBASE_CATALOG = "hbase.catalog";
    public static final String HBASE_ZK = "hbase.zookeepers";
    public static final String ZK_HOST = "geomesa-hbase-local:2181";
    public static final String TABLE_NAME = "chunkruns_table";

    /**
     * used to make connection with geomesa-hbase.
     * @param tableName name of your table.
     * @return
     * @throws IOException
     */
    public static DataStore mkConnection(String tableName) throws IOException {
        Map<String, String> params = new HashMap<String, String>();

        params.put(ConfigProvider.HBASE_CATALOG, tableName);
        params.put(ConfigProvider.HBASE_ZK, ConfigProvider.ZK_HOST);

        return DataStoreFinder.getDataStore(params);
    }
}
