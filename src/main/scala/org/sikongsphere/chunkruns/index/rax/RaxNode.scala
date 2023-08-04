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

import scala.collection.mutable

/**
  * Rax树节点
  * 
  * @author Ziming Zhang
  */
class RaxNode {
	/*标记根到当前节点的路径是否构成一个key*/
	private var isKey_ : Boolean = false
	private var keySegment_ : String = ""
    private var children_ : mutable.TreeSet[RaxNode] = mutable.TreeSet.empty(RaxNode.raxNodeOrdering)
	/*如果isKey_为true，则data_中保存了key对应的value*/
    private var value_ : Option[Array[Byte]] = None

	def isEmpty: Boolean = !isKey_ && children_.isEmpty

	def nonEmpty: Boolean = !isEmpty

	def isKey: Boolean = isKey_

	def keySegment: String = keySegment_

	def childrenNum: Int = children_.size

	def getValue: Option[Array[Byte]] = value_

	def needMerge: Boolean = !isKey_ && childrenNum == 1

	/**
	 * 更新节点key
	 */
	def setKeySegment(keySeg: String): Unit = keySegment_ = keySeg

	/**
	  * 更新节点的值
	  *
	  * @param value 新值
	  * @param overwrite 是否覆盖原有值
	  * @return 是否更新成功
	  */
	def setValue(value: Array[Byte], overwrite: Boolean): Boolean = {
		if(!isKey_ || overwrite) {
			isKey_ = true
			value_ = Some(value)
			return true
		}
		false
	}

	def removeValue(): Option[Array[Byte]] = {
		val res = value_
		isKey_ = false
		value_ = None
		res
	}

	/**
	  * 返回当前节点所有的子节点
	  *
	  * @return
	  */
	def getChildren: List[RaxNode] = children_.toList

	/**
	 *
	 * @return 第一个子节点
	 */
	def getFirstChild: Option[RaxNode] = {
		children_.headOption
	}

	/**
	 * 找到能与keySeq大于一个字符长度前缀匹配的子节点
	 *
	 * @param keySeg 需要匹配的字符串
	 * @return 匹配到的子节点
	 */
	def findMatchChild(keySeg: String): Option[RaxNode] = {
		for(child <- children_) {
			if(child.keySegment_.charAt(0) == keySeg.charAt(0)) return Some(child)
		}
		None
	}

	def removeChild(child: RaxNode): Unit = {
		children_ = children_.filter(_.keySegment_ != child.keySegment_)
	}

    def addChild(child: RaxNode): Unit = {
		children_ += child
    }

	/**
	  * 对压缩节点，在第pos个字符处进行分裂
	  *
	  * @param pos 拆分位置
	  * @return 拆分后得到的头节点，表示原节点的前半部分。只存在一个子节点，即原节点的后半部分
	  */
	def splitAt(pos: Int): RaxNode = {
		assert(pos >= 0 && pos < keySegment_.length)

		var child1: RaxNode = RaxNode(keySegment_.substring(0, pos))
		var child2: RaxNode = RaxNode(keySegment_.substring(pos))
		child2.children_ = children_
		child2.isKey_ = isKey_
		child2.value_ = value_
		child1.addChild(child2)
		child1
	}

	def mergeWith(child: RaxNode): Unit = {
		assert(children_.contains(child))
		assert(!isKey_)
		assert(childrenNum == 1)

		keySegment_ += child.keySegment_
		children_ = child.children_
		value_ = child.value_
		isKey_ = child.isKey_
	}

	override def toString: String = {
		var res = "\"" + keySegment_ + "\""
		if(isKey_) {
			val valueStr = new String(value_.get)
			res += "[" + valueStr + "]"
		}
		res
	}

}

object RaxNode {
	def empty: RaxNode = RaxNode()

    def apply(): RaxNode = {
        val obj: RaxNode = new RaxNode()
        obj
    }

	def apply(keySeg: String): RaxNode = {
		val obj: RaxNode = new RaxNode()
		obj.keySegment_ = keySeg
		obj
	}

	implicit val raxNodeOrdering: Ordering[RaxNode] = Ordering.by(_.keySegment_)
}