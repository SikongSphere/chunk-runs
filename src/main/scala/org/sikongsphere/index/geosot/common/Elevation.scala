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
package org.sikongsphere.index.geosot.common

/**
 * GeoSOT中高程的封装类。
 * 设定高度单位是度分秒，参考椭球参数，建立空间高度单位与千米、米的换算关系：D˚×πR/180˚=H km。
 * 其中，R为地球平均半径，H为地心与该经纬度平面的高程。
 *
 * @author Ziming Zhang
 */
class Elevation extends Dimension {
    private final val EARTH_RADIUS = 6302.106722602182

    override def getValue(precision: Int): Int = 0
}

object Elevation {
    /**
     *
     * @param elevation
     * @return
     */
    def apply(elevation: String) = {
        ???
    }
}