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


/**
  * Rax实现
  */
class RaxTree {

	private var root_ : RaxNode = RaxNode()

	def find(key: String): Option[Array[Byte]] = {
		val WalkResult(raxStack: List[RaxNode], splitPos: Int, matchLen: Int) = lowWalk(key)
		raxStack.head.getValue
	}

	def insert(key: String, value: Array[Byte]): Boolean = {
		genericInsert(key, value, overwrite = true)
	}

	def tryInsert(key: String, value: Array[Byte]): Boolean = {
		genericInsert(key, value, overwrite = false)
	}

	private def genericInsert(key: String, value: Array[Byte], overwrite: Boolean): Boolean = {
		assert(key.nonEmpty)

		var WalkResult(raxStack: List[RaxNode], splitPos: Int, matchLen: Int) = lowWalk(key)
		val node = raxStack.head
		raxStack = raxStack.tail

		if (node.keySegment.isEmpty) {
			/*空串只会出现在根节点，如果根节点为空则生成新节点替换根节点，否则将新节点作为子节点插入*/
			val newNode = RaxNode(key)
			if(node.isEmpty) {
				root_ = newNode
				return root_.setValue(value, overwrite)
			}
			else {
				root_.addChild(newNode)
				return newNode.setValue(value, overwrite)
			}
		}

		if (matchLen == key.length && node.keySegment.length == splitPos) {
			/*在node处形成了完整key匹配，将value插入该node中*/
			node.setValue(value, overwrite = true)
		}
		else if (matchLen == key.length && node.keySegment.length > splitPos) {
			/*
			 * key与当前节点的前缀匹配，需要从中间分裂，向节点分裂后的前半段插入数据。
			 *
			 */
			val newNode = node.splitAt(splitPos)
			replaceSplitNode(raxStack, node, newNode)
			newNode.setValue(value, overwrite)
		}
		else if(matchLen < key.length && node.keySegment.length == splitPos) {
			/*当前节点是key的前缀，但没有可以进一步搜索的子节点，创建新节点插入到当前节点中*/
			val newNode = RaxNode(key.substring(matchLen))
			node.addChild(newNode)
			newNode.setValue(value, overwrite)
		}
		else {
			/*
			 * 没有形成完整匹配，需要将当前节点分裂成前后两半部分，前半部分是原节点与key的共同前缀
			 * 为key没有匹配的后半部分创建新节点并插入到前半部分节点中。
			 */
			val newNode = node.splitAt(splitPos)
			val keyNode = RaxNode(key.substring(matchLen))
			newNode.addChild(keyNode)
			replaceSplitNode(raxStack, node, newNode)
			keyNode.setValue(value, overwrite)
		}
	}

	/**
	 * 分裂后的节点替换原节点
	 *
	 * @param raxStack
	 * @param removedNode
	 * @param newNode
	 */
	private def replaceSplitNode(raxStack: List[RaxNode], removedNode: RaxNode, newNode: RaxNode): Unit = {
		if (raxStack.nonEmpty) {
			raxStack.head.removeChild(removedNode)
			raxStack.head.addChild(newNode)
		}
		else {
			root_ = newNode
		}
	}

	private case class WalkResult(raxStack: List[RaxNode], splitPos: Int, matchLen: Int)

	/**
	 * 对树进行深度优先遍历，返回最后一个访问的节点，以及匹配到key的长度
	 * 
	 */
	private def lowWalk(key: String): WalkResult = {
		assert(key.nonEmpty)

		var node: RaxNode = root_
		var splitPos: Int = 0
		var matchLen: Int = 0
		var keyPostfix: String = key
		var raxStack: List[RaxNode] = List[RaxNode]()
		raxStack = node +: raxStack

		while(matchLen < key.length) {
			splitPos = 0

			/*对当前节点的keySegment进行匹配*/
			val upperBound: Int = math.min(node.keySegment.length(), keyPostfix.length())
			for(pos <- 0 until upperBound) {
				if(node.keySegment.charAt(pos) != keyPostfix.charAt(pos)) {
					return WalkResult(raxStack, pos, matchLen)
				}
				matchLen += 1
				splitPos = pos + 1
			}
			keyPostfix = key.substring(matchLen)

			/* 
			 * Case1: abcd, abc
			*/
			if(matchLen == key.length()) {
				return WalkResult(raxStack, splitPos, matchLen)
			}

			/*
			 * Case2: abcd, abx
			 * 继续到子节点查找
			 * */
			node.findMatchChild(keyPostfix) match {
				case Some(child) => {
					/*匹配成功，继续向下遍历*/
					raxStack = child +: raxStack
					node = child
				}
				case None => {
					return WalkResult(raxStack, splitPos, matchLen)
				}
			}
		}
		WalkResult(raxStack, splitPos, matchLen)
	}

	def printTreeView(): Unit = {
		printTree(root_, "")
	}

	private def printTree(root: RaxNode, prefix: String = ""): Unit = {
		var str = prefix  + root.toString

		val children = root.getChildren
		if(children.nonEmpty) {
			str += "->"
			printTree(children.head, str)
			for(child <- children.tail) {
				printTree(child,  " " * str.length)
			}
		}
		else {
			println(str)
		}
	}


}

object RaxTree {
	def apply(): RaxTree = {
		val obj = new RaxTree()
		obj
	}
}