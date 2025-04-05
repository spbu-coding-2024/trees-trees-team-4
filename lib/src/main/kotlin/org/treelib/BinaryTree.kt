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
		var node = start
		while (node != null) {
			node = when {
				key < node.key -> node.left
				key > node.key -> node.right
				else -> return node
			}
		}
		return null
	}

	abstract fun insert(key: K, data: V)
	abstract fun delete(key: K)
	fun iterator(): Iterator<V> = iterator {
		inorder(root)
	}

	private suspend fun SequenceScope<V>.inorder(node: Node<K, V, N>?) {
		if (node != null) {
			inorder(node.left)
			yield(node.data)
			inorder(node.right)
		}
	}

	fun next(): V {
		return iterator().next()
	}

	fun hasNext(): Boolean {
		return iterator().hasNext()
	}
}
