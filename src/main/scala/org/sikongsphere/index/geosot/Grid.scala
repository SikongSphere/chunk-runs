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
package org.sikongsphere.index.geosot

import org.sikongsphere.index.geosot.common._


/**
 * GeoSOT地球网格参考系统中的单个网格
 *
 * @example 将”度°分'秒\" 方位“格式的坐标转换为GeoSOT编码
 *          val grid = Grid("42°38'59.68" S", "1°32'0.06" E", 32)
 *          val geosotCode = grid.toString
 *
 * @author Ziming Zhang
 */
class Grid {
    private var latitude_ : Latitude = Latitude("0°0'0\" N")
    private var longitude_ : Longitude = Longitude("0°0'0\" E")
    private var elevation_ : Elevation = Elevation("0")
    private var level_ : Int = 32
    private var code_ : MortonCode = MortonCode(longitude_, latitude_, elevation_)

    override def toString: String = {
        ("G" + code_.toString).substring(0, level_)
    }

}


object Grid {
    /**
     *
     * @param lat 纬度
     * @param lon 经度
     * @param level 参考网格所属层级，取值0~32
     * @return 返回二维空间指定层级下对应的网格
     */
    def apply(lat: String, lon: String, level: Int): Grid = {
        val obj = new Grid
        obj.latitude_ = Latitude(lat)
        obj.longitude_ = Longitude(lon)
        obj.level_ = level
        obj.code_ = MortonCode(obj.longitude_, obj.latitude_)
        obj
    }

    /**
     *
     * @param lat   纬度
     * @param lon   经度
     * @param level 参考网格所属层级，取值0~32
     * @return 返回三维空间指定层级下对应的网格
     */
    def apply(lat: String, lon: String, elev: String, level: Int): Grid = {
        val obj = new Grid
        obj.latitude_ = Latitude(lat)
        obj.longitude_ = Longitude(lon)
        obj.elevation_ = Elevation(elev)
        obj.level_ = level
        obj.code_ = MortonCode(obj.longitude_, obj.latitude_, obj.elevation_)
        obj
    }
}

