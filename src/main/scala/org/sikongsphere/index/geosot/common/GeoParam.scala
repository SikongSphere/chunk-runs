package org.sikongsphere.index.geosot.common

import Math._

object GeoParam {
	/* 
	 * 地球长半轴，单位（米），对应公式中r_0
	 */
	final val EarthRadiusMeters: Double = 6378137
	final val theta_0: Double = PI / 180					 // 地球赤道面与地球自转轴夹角，单位（弧度），对应公式中theta_0
}