package geosot.common

import scala.beans.BeanProperty
import scala.util.matching.Regex

// 空间中可以表示为"度-分-秒"的坐标分量
abstract class Coordinate extends Dimension{
    // 匹配DMS格式坐标的正则表达式
    val regex_dms_ : Regex

    protected var degrees_ : Int = 0
    protected var minutes_ : Int = 0
    protected var seconds_ : Double = 0.0
    protected var direction_ : String = ""

    def degree = degrees_
    def minutes = minutes_
    def seconds = seconds_
    def direction = direction_

    // 接受 "1°32'0.06\" E" 格式的输入
    def parseFromString(dms: String, regex_dms: Regex) = {
        dms match {
            case regex_dms(degrees, minutes, seconds, _, direction) =>
                degrees_ = degrees.toInt
                minutes_ = minutes.toInt
                seconds_ = seconds.toDouble
                direction_ = direction
            case _ =>
                throw new IllegalArgumentException(s"${dms}")
        }
    }


    // 将"度-分-秒"编码到一个Int中
    protected def concatDegMinSec(): Int = {
        var res: Int = 0
        res = res | ((degrees_ & 0xFF) << 23)
        res = res | ((minutes_ & 0x3F) << 17)
        res = res | ((seconds_.toInt & 0x3F) << 11)
        var frac_part: Int = ((seconds_ - math.floor(seconds_)) * 2048).toInt
        println(frac_part)
        res = (res | frac_part)
        res
    }
}