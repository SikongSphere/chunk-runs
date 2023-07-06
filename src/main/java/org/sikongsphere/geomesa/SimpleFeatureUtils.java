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
import org.geotools.data.DataStoreFinder;
import org.sikongsphere.common.ConfigProvider;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Liang Chao
 * @date 2023/7/6 15:50
 */
public class SimpleFeatureUtils {

    /**
     *此方法用于连接HBase
     * @param tableName 用于创建主表的名称
     * @return Datastore 用于写入数据的容器
     */
    public DataStore mkConnection(String tableName) throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ConfigProvider.HBASE_CATALOG, tableName);
        params.put(ConfigProvider.HBASE_ZK, ConfigProvider.ZK_HOST);

        return DataStoreFinder.getDataStore(params);
    }
}
