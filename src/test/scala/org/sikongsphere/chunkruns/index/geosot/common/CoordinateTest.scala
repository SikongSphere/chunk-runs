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
package org.sikongsphere.chunkruns.index.geosot.common

import org.scalatest.FlatSpec


/**
 * 测试Coordinate类
 * @author Ziming Zhang
 */
class CoordinateTest extends FlatSpec {
    private val latitudes = Array[String](
        "42°38'59.68\" S",
        "42°35'15.54\" N",
        "42°33'0.11\" S",
        "42°28'31.87\" N"
    )

    private val longitudes = Array[String](
        "1°32'0.06\" E",
        "1°44'25.01\" W",
        "1°26'59.5\" W",
        "1°26'30.23\" E"
    )

    private val lat_ans = Array[(Int, Int, Double, String)](
        (42, 38, 59.68, "S"), (42, 35, 15.54, "N"), (42, 33, 0.11, "S"), (42, 28, 31.87, "N"),
    )

    private val lon_ans = Array[(Int, Int, Double, String)](
        (1, 32, 0.06, "E"), (1, 44, 25.01, "W"), (1, 26, 59.5, "W"), (1, 26, 30.23, "E")
    )

    behavior of "A Latitude"

    it should "parse the latitude of the string representation" in {
        for (i: Int <- Range(0, latitudes.length)) {
            val new_lat = Latitude(latitudes(i))
            val obtained = (new_lat.degree, new_lat.minutes, new_lat.seconds, new_lat.direction)
            val expected = lat_ans(i)
            assert(obtained === expected)
        }
    }

    private val one_dim_lat_code = Array[String]("2504908144", "356940881", "2504130785", "356056821")
    it should "splice degrees, minutes, and seconds into a 32-bit Int code" in {
        for (i: Int <- Range(0, latitudes.length)) {
            val new_lat = Latitude(latitudes(i))
            val obtained = java.lang.Integer.toUnsignedString(new_lat.getValue())
            val expected = one_dim_lat_code(i)
            assert(expected === obtained)
        }
    }
    
    behavior of "A Longitude"
    private val one_dim_lon_code = Array[String]("12583034", "2161690644", "2159401984", "11858391")
    it should "parse the longitude of the string representation" in {
        for (i: Int <- Range(0, longitudes.length)) {
            val new_lon = Longitude(longitudes(i))
            val obtained = (new_lon.degree, new_lon.minutes, new_lon.seconds, new_lon.direction)
            val expected = lon_ans(i)
            assert(expected === obtained)
        }
    }
    
    it should "splice degrees, minutes, and seconds into a 32-bit Int code" in {
        for (i: Int <- Range(0, longitudes.length)) {
            val new_lon = Longitude(longitudes(i))
            val obtained = java.lang.Integer.toUnsignedString(new_lon.getValue())
            val expected = one_dim_lon_code(i)
            assert(expected === obtained)
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