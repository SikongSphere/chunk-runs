package org.sikongsphere.index.geosot

/**
 * 这是一段简单的测试代码，在scalaTest能用之前暂时用这段代替
 *
 * @author Ziming Zhang
 */
object Main {
    private val coordinates = List[String](
        "42°38'59.68\" S, 1°32'0.06\" E",
        "42°35'15.54\" N, 1°44'25.01\" W",
        "39°54'37.0\" N, 116°18'54.8\" E"
    )
    private val geosot_code = List[String](
        "G20020202130022022202220203331010",
        "G10020202130112201322320002030102"
    )
    def main(args: Array[String]): Unit = {
        val LEVEL = 27
        for (i <- Range(0, coordinates.size)) {
            val lat_lon = coordinates(i).split(", ")
            val geosot_grid = Grid(lat_lon(0), lat_lon(1), LEVEL)
            var obtained = geosot_grid.toString
//            var expected = geosot_code(i)
//            assert(obtained == expected)
            println(obtained + ", " + s"${obtained.length}")
        }
    }
}
