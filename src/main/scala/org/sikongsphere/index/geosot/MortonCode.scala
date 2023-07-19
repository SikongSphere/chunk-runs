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
 * 莫顿编码的实现类
 *
 * @author Ziming Zhang
 */
class MortonCode {
    private final val COMP_LEN = 32
    private var data_ : Array[Int] = Array()

    /**
     * 对莫顿码进行解码
     *
     * @return 解码后得到的坐标分量
     */
    def decode(): Array[Int] = {
        simpleSeparation(data_)
    }

    /**
     * @return 二进制莫顿编码
     */
    def encode(): Array[Int] = data_

    /**
     * 按照ZYX的顺序将各个坐标分量的比特位交叠起来
     *
     * @example 比特交叠示例
     *          dims[0-7]: X0X1X2X3 | Y0Y1Y2Y3
     *          result[0-7]: X1Y1X2Y2 | X3Y3X4Y4
     * @return 返回交叠后的莫顿编码值
     */
    private def simpleInterleave(dims: Array[Dimension]): Array[Int] = {
        val dimension = dims.size
        var res: Array[Int] = Array.ofDim[Int](dimension)
        for (i: Int <- 0 to COMP_LEN - 1) {
            for (j: Int <- 0 to dimension - 1) {
                val index: Int = (i * dimension + j) / COMP_LEN
                res(index) = (res(index) | (((dims(j).getValue() >>> i) & 1) << (i * dimension + j) % COMP_LEN))
            }
        }
        return res
    }

    /**
     * 从交叠的编码中还原出的各个坐标分量
     *
     * @return 返回解码后坐标各个分量上的值
     * @example 比特交叠示例
     *          code[0-7]: X1Y1X2Y2 | X3Y3X4Y4
     *          dims[0-7]: X0X1X2X3 | Y0Y1Y2Y3
     */
    private def simpleSeparation(code: Array[Int]): Array[Int] = {
        val dimension: Int = code.size
        var res: Array[Int] = Array.ofDim[Int](dimension)
        for (i: Int <- 0 to COMP_LEN - 1) {
            for (j: Int <- 0 to dimension - 1) {
                val offset: Int = i * dimension + j
                res(j) = (res(j) | (((code(offset / COMP_LEN) >>> (offset % COMP_LEN)) & 1) << i))
            }
        }
        return res
    }

    override def toString: String = {
        toQuaternaryString
    }

    /**
     * @return 返回莫顿码按照4进制编码的字符串
     */
    private def toQuaternaryString: String = {
        val resultBuilder = new StringBuilder()
        val quaternaryDigits = "0123"

        for (i: Int <- data_.size - 1 to 0 by -1) {
            var tmpInt = data_(i)
            val byteStringBuilder = new StringBuilder()
            for (_ <- 1 to COMP_LEN / 2) {
                val digit = quaternaryDigits.charAt(tmpInt & 0x03)
                byteStringBuilder.insert(0, digit)
                tmpInt = (tmpInt >> 2)
            }
            resultBuilder.append(byteStringBuilder.toString())
        }
        resultBuilder.toString()
    }

}

object MortonCode {
    /**
     * @param dims 二进制表示的各坐标分量数组
     */
    def apply(dims: Array[Dimension]): MortonCode = {
        val obj = new MortonCode
        obj.data_ = obj.simpleInterleave(dims)
        obj
    }

    /**
     * @param longitude 经度
     * @param latitude 纬度
     */
    def apply(longitude: Longitude, latitude: Latitude): MortonCode = {
        val obj = MortonCode(Array[Dimension](longitude, latitude))
        obj
    }

    /**
     * @param longitude 经度
     * @param latitude  纬度
     * @param elevation 高程
     */
    def apply(longitude: Longitude, latitude: Latitude, elevation: Elevation): MortonCode = {
        val obj = MortonCode(Array[Dimension](longitude, latitude, elevation))
        obj
    }

    /**
     * @param code 二进制表示的莫顿编码
     */
    def apply(code: Array[Int]): MortonCode = {
        val obj = new MortonCode
        obj.data_ = code
        obj
    }
}
