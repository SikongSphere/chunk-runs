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

import Math.PI

object GeoParam {
	/**
	 * 地球长半轴，单位（米），对应公式中r_0
	 */
	final val EarthRadiusMeters: Double = 6378137

	/**
	 * 初始剖分定义的基础网格（即第9级网格）对应的经（纬）度差，单位（弧度）
	 */
	final val THETA_0: Double = PI / 180

	/**
	 * TODO: 该参数的计算方法在标准中并没有说明，需要进一步验证
	 * @param level 网格层数
	 * @return 网格对应的经纬跨度差
	 */
	def theta(level: Int): Double = {
		val res = Math.pow(2, 9 - level) * THETA_0
		res
	}
}