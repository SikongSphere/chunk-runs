package geosot


class GridTest extends munit.FunSuite {
    private val coordinates = List[String](
        "42°38'59.68\" S, 1°32'0.06\" E",
        "42°35'15.54\" N, 1°44'25.01\" W"
    )
    private val geosot_code = List[String](
        "G20020202130022022202220203331010",
        "G10020202130112201322320002030102"
    )

    test("Precision32") {
        for(i <- Range(0, coordinates.size)) {
            val lat_lon = coordinates(i).split(", ")
            val geosot_grid = new Grid(lat_lon(0), lat_lon(1))
            var obtained = geosot_grid.toString
            var expected = geosot_code(i)
            assert(clue(obtained) == clue(expected))
        }
    }

}