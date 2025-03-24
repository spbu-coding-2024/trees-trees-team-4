package org.treelib

abstract class BinaryTree<K : Comparable<K>, V : Any, N : Node<K, V, N>>() {
	internal var root: Node<K, V, N>? = null
	fun min(start: Node<K, V, N>? = root): Node<K, V, N>? {
		var resultNode = start ?: return null
		while (true) {
			resultNode = resultNode.left ?: break
		}
		return resultNode
	}


	fun max(start: Node<K, V, N>? = root): Node<K, V, N>? {
		var resultNode = start ?: return null
		while (true) {
			resultNode = resultNode.right ?: break
		}
		return resultNode
	}

	abstract fun insert(key: K, data: V): N?
	abstract fun search(key: K): N?
	abstract fun delete(key: K): N?
	abstract fun iterator(key: K): Iterable<N>

}
