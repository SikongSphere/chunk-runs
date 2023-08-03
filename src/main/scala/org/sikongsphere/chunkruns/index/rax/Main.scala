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
//package org.sikongsphere.chunkruns.index.rax

import org.sikongsphere.chunkruns.index.rax.RaxTree
import scala.io.StdIn
import java.io._

object Main {
    def main(args: Array[String]): Unit = {
//        while(true) {
//            print("RaxTest ~> ")
//            val cmd = StdIn.readLine()
//            println(cmd)

       testFromFile()
       testFromArray()
    }

    private def testFromArray(): Unit = {
        val dataArray: Array[(String, String)] = Array(
            ("nil", "0x0"),
            ("alien", "0x1"),
            ("baloon", "0x2"),
            ("chromodynamic", "0x3"),
            ("romane", "0x4"),
            ("romanus", "0x5"),
            ("romulus", "0x6"),
            ("rubens", "0x7"),
            ("ruber", "0x8"),
            ("rubicon", "0x9"),
            ("rubicundus", "0xa"),
            ("all", "0xb"),
            ("rub", "0xc"),
            ("ba", "0xd"),
            ("fasfa", "")
        )

        val raxTree = RaxTree()
        dataArray.foreach { case (name, value) =>
            raxTree.insert(name, value.getBytes())
            val res = raxTree.find(name)
            assert(res.nonEmpty)
            assert(res.get sameElements value.getBytes())
        }

        raxTree.printTreeView()
    }

    private def testFromFile(): Unit = {
        val dataFilePath = "src/test/resources/dataset/latlng-base32-32-1000.csv"
        val raxTree = RaxTree()


        val reader = new BufferedReader(new FileReader(dataFilePath))
        reader.readLine()
        try {
            var line: String = reader.readLine()
            while (line != null) {
                val items = line.split(", ")
                raxTree.insert(items(3), "".getBytes())
                line = reader.readLine()
            }
            raxTree.printTreeView()

        } catch {
            case e: IOException =>
                e.printStackTrace()
        } finally if (reader != null) reader.close()
    }
}
