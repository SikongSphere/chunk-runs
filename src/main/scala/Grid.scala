package geosot

import geosot.common._

class Grid() {

    private var latitude: Latitude = new Latitude("0°0'0\" N")
    private var longitude: Longitude = new Longitude("0°0'0\" E")
    private var elevation: Option[Elevation] = None
    private var code: Array[Byte] = Array.ofDim[Byte](8)

    def this(lat: String, lon: String) = {
        this()
        latitude = new Latitude(lat)
        longitude = new Longitude(lon)
        code = MortonCode.enCode(Array[Int](longitude.getValue(), latitude.getValue()))
    }
    def this(lat: String, lon: String, elev: String) = {
        this(lat, lon)
        val elev_inst = new Elevation(elev)
        elevation = Some(elev_inst)
        code = MortonCode.enCode(Array[Int](longitude.getValue(), latitude.getValue(), elev_inst.getValue()))
    }

    override def toString: String = {
        code.size match {
            case 8 => {
                var str_builder = new StringBuilder("G")
                for(i: Int <- 7 to 0 by -1) {
                    str_builder.append(byteToQuaternary(code(i)))
                }
                str_builder.toString()
            }
            case _ => "G000000000-000000-000000-00000000000"
        }
    }

    def byteToQuaternary(byte: Byte): String = {
        val quaternaryDigits = "0123"
        val resultBuilder = new StringBuilder()
        var tempByte = byte

        for (_ <- 1 to 4) {
            val digit = quaternaryDigits.charAt(tempByte & 0x03)
            resultBuilder.insert(0, digit)
            tempByte = (tempByte >> 2).toByte
        }
        resultBuilder.toString()
    }

}
