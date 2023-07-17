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

import munit.FunSuite

class CoordinateTest extends munit.FunSuite {
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

    test("Latitude.parseFromString") {
        for(i: Int <- Range(0, latitudes.size)) {
            val new_lat = new Latitude(latitudes(i))
            val obtained = (new_lat.degree, new_lat.minutes, new_lat.seconds, new_lat.direction)
            val expected = lat_ans(i)
            assert(clue(expected) == clue(obtained))
        }
    }

    test("Longitude.parseFromString") {
        for (i: Int <- Range(0, longitudes.size)) {
            val new_lon = new Longitude(longitudes(i))
            val obtained = (new_lon.degree, new_lon.minutes, new_lon.seconds, new_lon.direction)
            val expected = lon_ans(i)
            assert(clue(expected.toString) == clue(obtained.toString))
        }
    }

    private val one_dim_lat_code = Array[String]("2504908144", "356940881", "2504130785", "356056821")
    test("Latitude.getValue") {
        for (i: Int <- Range(0, latitudes.size)) {
            val new_lon = new Latitude(latitudes(i))
            val obtained = java.lang.Integer.toUnsignedString(new_lon.getValue())
            val expected = one_dim_lat_code(i)
            assert(clue(expected) == clue(obtained), s"${latitudes(i)}")
        }
    }

    private val one_dim_lon_code = Array[String]("12583034", "2161690644", "2159401984", "11858391")
    test("Longitude.getValue") {
        for (i: Int <- Range(0, longitudes.size)) {
            val new_lon = new Longitude(longitudes(i))
            val obtained = java.lang.Integer.toUnsignedString(new_lon.getValue())
            val expected = one_dim_lon_code(i)
            assert(clue(expected) == clue(obtained), s"${longitudes(i)}")
        }
    }
}
