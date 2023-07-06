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

import org.junit.Test;

import java.util.HashMap;

/**
 * @author Liang Chao
 * @date 2023/7/6 17:26
 */
public class IfcLonLatParserTest {

    private final static String ifcFile = "src/test/resources/Duplex_A_20110505.ifc";

    @Test
    public void parse() {
        HashMap<String, Double> coordination = IfcLonLatParser.parse(ifcFile);

        assert coordination.get("latitude") == 42.453955;
        assert coordination.get("longitude") == -71.1138061111111;
    }
}
