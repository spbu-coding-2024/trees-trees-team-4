package org.treelib

abstract class BinaryTree<K : Comparable<K>, D : Any?, N : Node<K, D?, N>> {
	var root: N? = null
		protected set

	fun findMin(start: N? = root): D? {
		var resultNode = start ?: return null
		while (true) {
			resultNode = resultNode.left ?: return resultNode.data
		}
	}

	fun findMax(start: N? = root): D? {
		var resultNode = start ?: return null
		while (true) {
			resultNode = resultNode.right ?: return resultNode.data
		}
	}

	protected fun findPredecessor(node: N): N?{
		var resultNode = node.left ?: return null
		while (true) {
			resultNode = resultNode.right ?: return resultNode
		}
	}

	fun search(key: K, start: N? = root): D? {
		var node = start
		while (node != null) {
			node = when {
				key < node.key -> node.left
				key > node.key -> node.right
				else -> return node.data
			}
		}
		return null
	}

	abstract fun insert(key: K, data: D?)
	abstract fun delete(key: K)
	operator fun iterator(): Iterator<D?> = iterator {
		inorder(root)
	}

	private suspend fun SequenceScope<D?>.inorder(node: Node<K, D?, N>?) {
		if (node != null) {
			inorder(node.left)
			yield(node.data)
			inorder(node.right)
		}
	}

	fun next(): D? {
		return iterator().next()
	}

	fun hasNext(): Boolean {
		return iterator().hasNext()
	}
}
