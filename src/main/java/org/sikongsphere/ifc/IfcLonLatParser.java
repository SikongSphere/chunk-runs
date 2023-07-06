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
package org.sikongsphere.ifc;

import org.sikongsphere.ifc.model.IfcAbstractClass;
import org.sikongsphere.ifc.model.datatype.INTEGER;
import org.sikongsphere.ifc.model.fileelement.IfcFileModel;
import org.sikongsphere.ifc.model.schema.extension.product.entities.IfcSite;
import org.sikongsphere.ifc.sdk.SikongSphereSession;
import org.sikongsphere.ifc.sdk.convert.IfcModelConvertor;
import org.sikongsphere.ifc.sdk.query.IfcModelQuery;

import java.util.HashMap;
import java.util.List;

/**
 * @author Liang Chao
 * @date 2023/7/5 11:46
 */
public class IfcLonLatParser {

    /**
     * convert (degrees, minutes, seconds, milliseconds) to degrees.
     * @param degrees degree
     * @param minutes minute
     * @param seconds second
     * @param milliseconds millisecond
     * @return degrees
     */
    private Double convertToDegree(
        Integer degrees,
        Integer minutes,
        Integer seconds,
        Integer milliseconds
    ) {
        double decimalMinutes = (double) minutes / 60;
        double decimalSeconds = (double) seconds / 3600;
        double decimalMilliseconds = (double) milliseconds / 3600000;

        return degrees + decimalMinutes + decimalSeconds + decimalMilliseconds;
    }

    /**
     * extract IfcSite from IfcModel.
     * @param ifcFile path to your ifc file.
     * @return IfcSite
     */
    private IfcSite getIfcSite(String ifcFile) {
        SikongSphereSession session = SikongSphereSession.getOrCreate();
        IfcModelConvertor convertor = (IfcModelConvertor) session.convertor(IfcFileModel.class);

        IfcFileModel model = convertor.readFile(ifcFile);
        IfcModelQuery query = (IfcModelQuery) session.query(IfcFileModel.class);
        List<IfcAbstractClass> objects = query.filterByClass(model, IfcSite.class);

        return (IfcSite) objects.get(0);
    }

    /**
     * returns a hashmap which contains coordination
     * @param ifcFile path to your ifc file.
     * @return .
     */
    public static HashMap<String, Double> parse(String ifcFile) {
        IfcLonLatParser parser = new IfcLonLatParser();
        IfcSite ifcSite = parser.getIfcSite(ifcFile);

        List<INTEGER> refLatitude = ifcSite.getRefLatitude().getObjects();
        List<INTEGER> refLongitude = ifcSite.getRefLongitude().getObjects();

        Double latitude = parser.convertToDegree(
            refLatitude.get(0).value,
            refLatitude.get(1).value,
            refLatitude.get(2).value,
            refLatitude.get(3).value
        );
        Double longitude = parser.convertToDegree(
            refLongitude.get(0).value,
            refLongitude.get(1).value,
            refLongitude.get(2).value,
            refLongitude.get(3).value
        );

        HashMap<String, Double> coordination = new HashMap<>();
        coordination.put("latitude", latitude);
        coordination.put("longitude", longitude);

        return coordination;
    }
}
