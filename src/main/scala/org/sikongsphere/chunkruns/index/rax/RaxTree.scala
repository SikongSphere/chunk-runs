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

import java.io._

/**
  * Rax实现
  */
class RaxTree {

	private var root_ : RaxNode = RaxNode()

	private var statCollector_ : Option[RaxStatCollector] = None

	def isEmpty: Boolean = root_.isEmpty

	def statCollectOn(): Unit = {
		if(statCollector_.isEmpty) statCollector_ = Some(new RaxStatCollector)
	}

	def find(key: String): Option[Array[Byte]] = {
		val startTime = System.nanoTime()
		val WalkResult(raxStack: List[RaxNode], splitPos: Int, matchLen: Int) = lowWalk(key)
		val endTime = System.nanoTime()

		if (statCollector_.nonEmpty) {
			statCollector_.get.incrQueryCost(endTime - startTime)
		}

		raxStack.head.getValue
	}

	def insert(key: String, value: Array[Byte]): Boolean = {
		val startTime = System.nanoTime()
		val res = genericInsert(key, value, overwrite = true)
		val endTime = System.nanoTime()

		if(res && statCollector_.nonEmpty) {
			statCollector_.get.incrInsertCost(key, endTime - startTime)
		}
		res
	}

	def tryInsert(key: String, value: Array[Byte]): Boolean = {
		val startTime = System.nanoTime()
		val res = genericInsert(key, value, overwrite = false)
		val endTime = System.nanoTime()

		if (res && statCollector_.nonEmpty) {
			statCollector_.get.incrInsertCost(key, endTime - startTime)
		}
		res
	}

	/**
	 * 移除指定元素，返回被移除元素的值
	 *
	 * @param key
	 * @return
	 */
	def remove(key: String): Option[Array[Byte]] = {
		val startTime: Long = System.nanoTime()
		val res = genericRemove(key)
		val endTime: Long = System.nanoTime()

		if(statCollector_.nonEmpty) {
			statCollector_.get.incrRemoveCost(key, endTime - startTime)
		}
		res
	}

	/**
	 * 移除元素的底层方法
	 *
	 * @param key
	 * @return
	 */
	private def genericRemove(key: String): Option[Array[Byte]] = {
		var WalkResult(raxStack: List[RaxNode], splitPos: Int, matchLen: Int) = lowWalk(key)
		var node = raxStack.head
		raxStack = raxStack.tail

		if (!node.isKey || matchLen != key.length || splitPos != node.keySegment.length) {
			return None
		}
		else {
			val res = node.removeValue()
			/*
             * 删除后的节点可能需要合并，合并有以下情况 :
             * 1.当前节点为空: 不是根节点则直接从父节点删除，否则删除根节点。这个合并过程是递归的，即如果父节点删除子节点造成父节点成为空节点，
             *   则需要继续将父节点删除。如果父节点可以和当前节点合并，则合并后直接返回
             * 2.当前节点不为空：
             * 2.1 当前节点只有一个子节点: 将子节点合并到当前节点
             * 2.2 父节点可以合并: 将当前节点合并到父节点
            */
			var parentOption: Option[RaxNode] = raxStack.headOption
			/*case1*/
			while (node.isEmpty) {
				parentOption match {
					case Some(parent) => {
						raxStack = raxStack.tail
						parent.removeChild(node)
						if (parent.needMerge) {
							parent.mergeWith(parent.getFirstChild.get)
							return res
						}
						else if (parent.isEmpty) {
							node = parent
							parentOption = raxStack.headOption
						}
						else return res
					}
					case None => {
						root_ = RaxNode("")
						return res
					}
				}
			}
			/*Case2*/
			if (node.nonEmpty) {
				/*合并当前节点的子节点*/
				if (node.needMerge) {
					var child: RaxNode = node.getChildren.head
					node.mergeWith(child)
				}
				/*合并到父节点*/
				parentOption match {
					case Some(parent) => {
						if (parent.needMerge) {
							parent.mergeWith(node)
						}
					}
				}
			}
			res
		}
	}

	def getTreeDepth: Int = getDepth(root_)

	private def getDepth(node: RaxNode): Int = {
		if(node.childrenNum == 0) {
			return 1
		}

		var depth = 0
		for(child <- node.getChildren) {
			val childDepth = getDepth(child)
			depth = if(childDepth > depth) childDepth else depth
		}
		depth + 1
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
			node.setValue(value, overwrite)
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

	def printTreeView(fileWriter: Option[Writer] = None): Unit = {
		if(fileWriter.nonEmpty) showStat(fileWriter)
		printTree(root_, "", fileWriter)
	}

	private def printTree(root: RaxNode, prefix: String = "", fileWriter: Option[Writer] = None): Unit = {
		var str = prefix  + root.toString

		val children = root.getChildren
		if(children.nonEmpty) {
			str += "->"
			printTree(children.head, str, fileWriter)
			for(child <- children.tail) {
				printTree(child,  " " * str.length, fileWriter)
			}
		}
		else {
			println(str)
			if(fileWriter.nonEmpty) fileWriter.get.write(str + "\n")
		}
	}

	private def showStat(fileWriter: Option[Writer] = None): Unit = {
		if (statCollector_.isEmpty) return

		val msg: List[String] = List(
			s"TreeDepth=${getTreeDepth}",
			s"TotalKeyNum=${statCollector_.get.getTotalKeyNum}",
			s"AverageKeyLen=${statCollector_.get.getAvgKeyLen}",
			s"InsertCount=${statCollector_.get.getInsertCount}",
			s"QueryCount=${statCollector_.get.getQueryCount}",
			s"RemoveCount=${statCollector_.get.getRemoveCount}",
			s"InsertTime=${statCollector_.get.getInsertCost}ns",
			s"QueryTime=${statCollector_.get.getQueryCost}ns",
			s"RemoveTime=${statCollector_.get.getRemoveCost}ns"
		)
		for(m <- msg) {
			println(m)
			if (fileWriter.nonEmpty) fileWriter.get.write(m + "\n")
		}
	}

}

object RaxTree {
	def apply(): RaxTree = {
		val obj = new RaxTree()
		obj
	}
}

/**
 * 采集rax tree的统计信息
 */
class RaxStatCollector {
	private var keyCount_ : Int = 0
	private var keyTotalLen_ : Int = 0
	private var insertCount_ : Int = 0
	private var queryCount_ : Int = 0
	private var removeCount_ : Int = 0
	private var insertConsumeTime_ : Long = 0
	private var queryConsumeTime_ : Long = 0
	private var removeConsumeTime_ : Long = 0

	def incrInsertCost(key: String, interval: Long): Unit = {
		keyCount_ += 1
		insertCount_ += 1
		keyTotalLen_ += key.length
		insertConsumeTime_ += interval
	}

	def incrQueryCost(interval: Long): Unit = {
		queryConsumeTime_ += interval
		queryCount_ += 1
	}

	def incrRemoveCost(key: String, interval: Long): Unit = {
		removeConsumeTime_ += interval
		keyCount_ -= 1
		keyTotalLen_ -= key.length
		removeCount_ += 1
	}

	def getInsertCost: Long = insertConsumeTime_

	def getQueryCost: Long = queryConsumeTime_

	def getRemoveCost: Long = removeConsumeTime_

	def getInsertCount: Int = insertCount_

	def getRemoveCount: Int = removeCount_

	def getQueryCount: Int = queryCount_

	def getTotalKeyNum: Int = keyCount_

	def getAvgKeyLen: Double = if(keyCount_ > 0) keyTotalLen_ / keyCount_ else 0
}