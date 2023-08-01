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

import scala.reflect.io.File

/**
 * 测试Grid类
 * @author Ziming Zhang
 */
class GridTest extends FlatSpec {

    behavior of "A GeoSOT Grid"

    it should "converts coordinate points to quadratic GeoSOT code" in {
        for(level: Int <- 0 to 32) {
            val basePath = "src\\test\\resources\\dataset\\"
            val fileName = s"level-${level}-size1000.csv"
            val lines = File(basePath + fileName).lines().drop(1)
            println(fileName)
            for(line <- lines) {
                val items = line.split(", ")
                val geosot_grid = Grid(items(0), items(1), level)
                var obtained = geosot_grid.toString
                var expected = items(2)
                assert(obtained === expected)
            }
        }
    }

    it should "converts coordinate points with elevation to GeoSOT-3D code" in {
        for (level: Int <- 15 to 32) {
            val basePath = "src\\test\\resources\\dataset\\"
            val fileName = s"level-${level}-size1000.csv"
            val height = "998.8m"
            val lines = File(basePath + fileName).lines().drop(1)
            for (line <- lines) {
                val items = line.split(", ")
                val grid = Grid(items(0), items(1), height, level)
                var obtained = grid.toString
                var expected = items(2)
//                assert(obtained === expected)
                println(s"[TEST]level=${level}, height=${height}, layer=${grid.getGridLayer} " + obtained)
            }
        }
    }
}