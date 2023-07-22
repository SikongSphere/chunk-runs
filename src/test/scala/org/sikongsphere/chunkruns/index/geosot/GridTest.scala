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
package org.sikongsphere.chunkruns.index.geosot

import org.scalatest.FlatSpec

/**
 * 测试Grid类
 * @author Ziming Zhang
 */
class GridTest extends FlatSpec {
    private val coordinates = List[String](
        "42°38'59.68\" S, 1°32'0.06\" E",
        "42°35'15.54\" N, 1°44'25.01\" W"
    )

    private val geosot_code = List[String](
        "G20020202130022022202220203331010",
        "G10020202130112201322320002030102"
    )

    behavior of "A GeoSOT Grid"

    it should "convert coordinate points to quadratic geosot code" in {
//        assert(Set.empty.size === 0)
        val LEVEL = 32
        for (i <- Range(0, coordinates.size)) {
            val lat_lon = coordinates(i).split(", ")
            val geosot_grid = Grid(lat_lon(0), lat_lon(1), LEVEL)
            var obtained = geosot_grid.toString
            var expected = geosot_code(i)
            assert(obtained === expected)
        }
    }
}