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

import org.scalatest.{FunSpec, ShouldMatchers}
import org.sikongsphere.index.geosot.common._


class MortonCodeTest extends FunSpec with ShouldMatchers {
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

    describe("GeoSOT.Morton.Encode") {
        it("can convert coordinate points to morton code") {
            for (i <- Range(0, coordinates.size)) {
                val code = MortonCode(Longitude(coordinates(i)._1), Latitude(coordinates(i)._2))
                var obtained = code.toString
                var expected = quadCode(i)
                obtained should be(expected)
            }
        }
    }

    describe("GeoSOT.Morton.Decode") {
        it("can convert morton code to coordinate points")
        for (i <- Range(0, quadCode.size)) {
            val mortonCode = quaternaryStringToBinaryCode(quadCode(i))
            val code = MortonCode(mortonCode)
            val res = code.decode()
            var obtained = (res(0), res(1))
            var expected = coordinates(i)
            obtained should be(expected)
        }
    }

    def quaternaryStringToBinaryCode(code: String): Array[Int] = {
        val mortonCode = code.substring(0)
        val reversedCode = mortonCode.reverse + "0" * (32 - mortonCode.size)
        val res = Array.ofDim[Int](reversedCode.size * 2 / 32)
        for (i <- Range(0, reversedCode.size)) {
            val int_idx = i * 2 / 32
            val num = (reversedCode(i) - '0') & 0x3
            res(int_idx) = res(int_idx) | (num << (i * 2 % 32))
        }
        res
    }
}