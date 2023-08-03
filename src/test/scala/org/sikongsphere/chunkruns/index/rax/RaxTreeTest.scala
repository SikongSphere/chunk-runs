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
package org.sikongsphere.chunkruns.index.rax

import org.scalatest.FlatSpec


class RaxTreeTest extends FlatSpec{
    behavior of "A RaxTree"

    it should "Store key-value pairs and query them by key" in {
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
    }

}