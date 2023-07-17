package geosot.common

import scala.util.matching.Regex

/**
 * 纬度的封装类
 *
 * @author Ziming Zhang
 * @date 2023/7/17 21:32
 */
class Latitude extends Coordinate {
    private val _regex_dms : Regex = """\s*(\d+)°(\d+)'(\d+(\.\d+)?)"\s([NS])\s*""".r
}

object Latitude {
    /**
     * @param dms "{度}°{分}'{秒}\" {方位}"格式的String
     */
    def apply(dms: String) = {
        var lat = new Latitude
        lat.parseFromString(dms, lat._regex_dms)
        var res: Int = lat.concatDegMinSec()
        if (lat.direction == "S") {
            res = res | (1 << 31)
        }
        lat.value_ = res
        lat
    }

    /**
     * @param value 32位编码的坐标信息，度占8位，分和秒分别占6位，小数点后的数字精确到1/2048秒，占用11位
     * @see Coordinate
     */
    def apply(value: Int) = {
        val obj = new Latitude
        obj.direction_ = if (value >>> 31 == 1) "S" else "N"
        obj.splitDegMinSec(value)
        obj.value_ = value
        obj
    }
}

