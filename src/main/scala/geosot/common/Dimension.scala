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

package geosot.common

/**
 * 任意可以以32位Int值表示的坐标分量的抽象，实现该特质的类可作为莫顿编码的维度分量
 *
 * @author Ziming Zhang
 * @date 2023/7/17 21:57
 */
trait Dimension {
    var value: Option[Int] = None
    def getValue(precision: Int = 32) : Int
}