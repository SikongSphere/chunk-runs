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

    it should "Store key-value pairs and query them by key" in {
        val raxTree = RaxTree()
        dataArray.foreach { case (name, value) =>
            raxTree.insert(name, value.getBytes())
            val res = raxTree.find(name)
            assert(res.nonEmpty)
            assert(res.get sameElements value.getBytes())
        }
    }

    it should "not update a value that is already associated with a key" in {
        val raxTree = RaxTree()
        dataArray.foreach { case (name, value) =>
            raxTree.insert(name, value.getBytes())
            val res = raxTree.find(name)
            assert(res.nonEmpty)
            assert(res.get sameElements value.getBytes())
        }

        assert(!raxTree.tryInsert("rub", "0xf".getBytes()))
        val str = new String(raxTree.find("rub").get)
        assert(str == "0xc")
    }

    it should "delete a given key and return the value associated" in {
        val raxTree = RaxTree()

        dataArray.foreach { case (name, value) =>
            raxTree.insert(name, value.getBytes())
        }
        dataArray.foreach {
            case (name, value) => {
                val removedValue: String = new String(raxTree.remove(name).getOrElse(Array[Byte]()))
                assert(removedValue == value)
            }
        }
        assert(raxTree.isEmpty)
    }

}