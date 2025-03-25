package org.treelib

abstract class BinaryTree<K : Comparable<K>, V : Any, N : Node<K, V, N>>(internal open var root: N? = null) {
	fun findMin(start: N? = root): N? {
		var resultNode = start ?: return null
		while (true) {
			resultNode = resultNode.left ?: return resultNode
		}
	}


	fun findMax(start: N? = root): N? {
		var resultNode = start ?: return null
		while (true) {
			resultNode = resultNode.right ?: return resultNode
		}
	}

	fun search(key: K, start: N? = root): N? {
		var resultNode = start ?: return null
		while (true) {
			when {
				key < resultNode.key -> resultNode = resultNode.left ?: return null
				key > resultNode.key -> resultNode = resultNode.right ?: return null
				key == resultNode.key -> return resultNode
			}
		}
	}

	abstract fun insert(key: K, data: V): N?
	abstract fun delete(key: K): N?
	abstract fun iterator(key: K): Iterable<N>

}
