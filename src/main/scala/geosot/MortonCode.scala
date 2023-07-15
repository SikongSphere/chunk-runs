package geosot


object MortonCode {
    private val coord_comp_len = 32
    def enCode(coordnate: Array[Int]): Array[Byte] = {
        simpleInterleave(coordnate)
    }

    def deCode(code: Array[Byte]): Array[Int] = {
        simpleSeparation(code)
    }

    private def simpleInterleave(coordinate: Array[Int]): Array[Byte] = {
        val dimension = coordinate.size
		var res: Array[Byte] = Array.ofDim[Byte](dimension * coord_comp_len / 8)
        for(i: Int <- 0 to coord_comp_len - 1){
            for(j: Int <- 0 to dimension - 1) {
                val byte_index: Int = (i * dimension + j) / 8
                res(byte_index) = (res(byte_index) | (((coordinate(j) >>> i) & 1) << (i * dimension + j) % 8)).toByte
            }
        }
        return res
    }

    private def simpleSeparation(code: Array[Byte]): Array[Int] = {
        if(code.size*8 % coord_comp_len != 0) {
            throw new IllegalArgumentException("Invalid argument: encoding length is not aligned to the component length.")
        }

        val dimension: Int = code.size * 8 / coord_comp_len
        var res: Array[Int] = Array.ofDim[Int](dimension)
        for (i: Int <- 0 to coord_comp_len - 1) {
            for (j: Int <- 0 to dimension - 1) {
                res(j) = (res(j) | (((code((i * dimension + j) / 8) >>> ((i * dimension + j)  % 8)) & 1) << i)).toInt
            }
        }
        return res
    }

    // TODO: 实现查找表法编码

}
