package geosot


class MortonCodeTest extends munit.FunSuite {
    private val morton_coder = MortonCode

    private val coordinates = List[Array[Int]](
        Array[Int](-1, -1, -1),
        Array[Int](1, 2, 3)
    )
    private val morton_code = List[Array[Byte]](
        Array[Byte](-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
        Array[Byte](53, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    )

    test("Encode") {
        for(i <- Range(0, coordinates.size)) {
            var obtained = morton_coder.enCode(coordinates(i))
            var expected = morton_code(i)
            assert(clue(obtained.size) == clue(expected.size))
            assert(clue(obtained.mkString(",")) == clue(expected.mkString(",")))
        }
    }

    test("Decode") {
        for (i <- Range(0, coordinates.size)) {
            var obtained = morton_coder.deCode(morton_code(i))
            var expected = coordinates(i)
            assert(clue(obtained.size) == clue(expected.size))
            assert(clue(obtained.mkString(",")) == clue(expected.mkString(",")))
        }
    }

}