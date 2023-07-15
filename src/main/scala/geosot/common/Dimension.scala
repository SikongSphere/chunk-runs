package geosot.common

import scala.beans.BeanProperty

// 点的维度分量，可能是坐标或时间
trait Dimension {
    // 获取指定精度的二进制编码
    def getValue(precision: Int = 32) : Int
}
