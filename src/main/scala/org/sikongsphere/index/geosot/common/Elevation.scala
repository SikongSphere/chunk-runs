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

import Math.PI
import GeoParam._

/**
 * GeoSOT中高程的封装类。
 * 设定高度单位是度分秒，参考椭球参数，建立空间高度单位与千米、米的换算关系：D˚×πR/180˚=H km。
 * 其中，R为地球平均半径，H为地心与该经纬度平面的高程。
 *
 * @author Ziming Zhang
 */
class Elevation extends Dimension {
	private var height_ : Double = 0

    override def getValue(precision: Int): Int = {
			(Math.log((height_ + GeoParam.EarthRadiusMeters) / GeoParam.EarthRadiusMeters) / Math.log(1 + GeoParam.theta_0)).toInt
	}
}

object Elevation {
    /**
     *
     * @param elevation "GB/T 16831规定的高程表示，单位是米"
     * @return
     * @example
     *      val elev = "978.90m"
     *      elevation = Elevation(elev)
     */
    def apply(elevation: String): Elevation = {
        val obj = new Elevation
        obj
    }

	
	/*
	 * 获取m级网格在大地高为H处高度域参数，遵照GB/T 40087-2021中6.3.2小节中的公式(5)
	 * @param H 大地高，单位（米）
	*  @param m 网格层级
     */
	def getLayerNumberAtAltitude(H: Double, m: Int): Int = {
		var res: Int = 0
		res
	}
}