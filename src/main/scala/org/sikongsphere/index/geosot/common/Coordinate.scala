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

import scala.util.matching.Regex


/**
 * 任何可以以”度-分-秒“表示的坐标分量的抽象
 *
 * @author Ziming Zhang
 */
abstract class Coordinate extends Dimension{
    protected var degrees_ = 0
    protected var minutes_ = 0
    protected var seconds_ = 0.0
    protected var direction_ = ""
    protected var value_ = 0

    def degree = degrees_
    def minutes = minutes_
    def seconds = seconds_
    def direction = direction_

    /**
     * 将"{度}°{分}'{秒}\" {方位}"格式的字符串解析为内部表示
     * @throws IllegalArgumentException 当参数不合法时抛出异常
     */
    protected def parseFromString(dms: String, regex_dms: Regex = """\s*(\d+)°(\d+)'(\d+(\.\d+)?)"\s([NSEW])\s*""".r) = {
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


    /**
     * 将坐标中的“度-分-秒”拼接为一个32位长的编码，度占8位，分和秒分别占6位，小数点后的数字精确到1/2048秒，占用11位
     * 编码的第32位作为符号位留给子类补充
     * @return 32位“度-分-秒”拼接编码
     */
    protected def concatDegMinSec(): Int = {
        var res = 0
        res = res | ((degrees_ & 0xFF) << 23)
        res = res | ((minutes_ & 0x3F) << 17)
        res = res | ((seconds_.toInt & 0x3F) << 11)
        val frac_part = ((seconds_ - math.floor(seconds_)) * 2048).toInt
        res = res | frac_part
        res
    }

    /**
     * @param value 编码为整型的坐标
     * 将编码为整型的坐标分解为“度-分-秒”
     */
    protected def splitDegMinSec(value: Int) = {
        val frac_part = value & 0x7FF
        val seconds = (value >>> 11) & 0x3F
        seconds_ = seconds.toDouble + frac_part.toDouble / 2048
        minutes_ = (value >>> 17) & 0x3F
        degrees_ = (value >>> 23) & 0xFF
    }

    /**
     * @param level 指定编码的精度，取值为0~32
     * @return 按照指定的精度返回编码后的坐标信息
     */
    override def getValue(level: Int = 32): Int = (value_ >>> (32 - level)) << (32 - level)

    /**
     *
     * 返回"{度}°{分}'{秒}\" {方位}"格式的字符串"
     */
    override def toString = {
        val sb = new StringBuilder()
        sb.append(degrees_.toString + "°")
        sb.append(minutes_.toString + "\'")
        sb.append(seconds_.toString + "\"")
        sb.append(" " + direction_)
        sb.toString
    }
}