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
package org.sikongsphere.chunkruns.index.geosot.common

/**
 * GeoSOT中高程的封装类。
 * 设定高度单位是度分秒，参考椭球参数，建立空间高度单位与千米、米的换算关系：D˚×πR/180˚=H km。
 * 其中，R为地球平均半径，H为地心与该经纬度平面的高程。
 *
 * @author Ziming Zhang
 */
class Elevation extends Dimension {
	private var height_ : Double = 0
	private var value_ : Option[Int] = None

	def height: Double = height_

	/**
	 * 获取网格的高度域编码
	 * @param precision 网格划分的精度，即递归划分的层级
	 * @return
	 */
    override def getValue(precision: Int): Int = {
		value_ match {
			case None => {
				value_ = Some(getLayerNumberAtGeodeticHeight(height_, precision))
				value_.get
			}
			case Some(value) => {
				value
			}
		}
	}

	/**
	* 获取m级网格在大地高为H处高度域参数，遵照GB/T 40087-2021中6.3.2小节中的公式(5)实现
	* @param height 大地高，单位（米）
	* @param level 网格层级
	*/
	private def getLayerNumberAtGeodeticHeight(height: Double, level: Int): Int = {
		val c = GeoParam.theta(level)
		val res = (c * Math.log((height + GeoParam.EarthRadiusMeters) / GeoParam.EarthRadiusMeters) / Math.log(1 + GeoParam.THETA_0))
		res.toInt
	}

	override def toString: String = {
		height_.toString + "m"
	}
}


object Elevation {
	private val regex_elev_  = """(\d+(\.\d+)?)(m)""".r

    /**
     *
     * @param elevation "GB/T 16831规定的高程表示，单位是米"
     * @example
     *      val elev = "978.90m"
     *      elevation = Elevation(elev)
     */
    def apply(elevation: String): Elevation = {
		elevation match {
			case regex_elev_(height, _, unit) => {
				val obj = new Elevation
				if(unit == "m") {
					obj.height_ = height.toDouble
				}
				obj
			}
			case _: String => {
				throw new IllegalArgumentException("Elevation formatting error")
			}
		}
    }
}