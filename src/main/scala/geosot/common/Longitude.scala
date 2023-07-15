package geosot.common

import scala.util.matching.Regex

class Longitude(dms: String) extends Coordinate {
    val regex_dms_ : Regex = """(\d+)°(\d+)'(\d+(\.\d+)?)"\s([EW])""".r

    super.parseFromString(dms, regex_dms_)

    //转换为指定精度的二进制编码
    override def getValue(precision: Int = 32): Int = {
        var res: Int = concatDegMinSec()
        if (direction == "W") {
            res = res | (1 << 31)
        }
        val shift = 32 - precision
        (res >>> shift) << shift
    }
}
