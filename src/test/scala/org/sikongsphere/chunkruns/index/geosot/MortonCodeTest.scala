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
package org.sikongsphere.chunkruns.index.geosot

import org.scalatest.FlatSpec
import org.sikongsphere.chunkruns.index.geosot.common._


/**
 * 测试MortonCode类
 * @author Ziming Zhang
 */
class MortonCodeTest extends FlatSpec {
    private val coordinates = List[(Int, Int)](
        (0xFFFFFFFF, 0xFFFFFFFF),
        (0xC0007A, 0x954DDD70),
        (0x80D8C814, 0x15467C51)
    )

    private val quadCode = List[String](
        "33333333333333333333333333333333",
        "20020202130022022202220203331010",
        "10020202130112201322320002030102"
    )

    behavior of "A Morton Code"

    it should "convert coordinate points to morton code" in {
        for (i <- Range(0, coordinates.length)) {
            val code = MortonCode(Longitude(coordinates(i)._1), Latitude(coordinates(i)._2))
            var obtained = code.toString
            var expected = quadCode(i)
            assert(obtained === expected)
        }
    }

    it should "convert morton code to coordinate points" in {
        for (i <- Range(0, quadCode.length)) {
            val mortonCode = quaternaryStringToBinaryCode(quadCode(i))
            val code = MortonCode(mortonCode)
            val res = code.decode()
            var obtained = (res(0), res(1))
            var expected = coordinates(i)
            assert(obtained === expected)
        }
    }

    it should "produce NoSuchElementException when head is invoked" in {
        assertThrows[NoSuchElementException] {
            Set.empty.head
        }
    }

    def quaternaryStringToBinaryCode(code: String): Array[Int] = {
        val mortonCode = code.substring(0)
        val reversedCode = mortonCode.reverse + "0" * (32 - mortonCode.length)
        val res = Array.ofDim[Int](reversedCode.length * 2 / 32)
        for (i <- Range(0, reversedCode.length)) {
            val int_idx = i * 2 / 32
            val num = (reversedCode(i) - '0') & 0x3
            res(int_idx) = res(int_idx) | (num << (i * 2 % 32))
        }
        res
    }
}