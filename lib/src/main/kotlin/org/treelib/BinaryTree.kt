package org.treelib

/**
 * An abstract binary tree that provides basic operations for finding minimum and maximum data,
 * searching for a key, and iterating over the data.
 *
 * @param K the type of keys maintained by this tree.
 * @param D the type of mapped data (nullable).
 * @param N the type of node used in the tree.
 */
abstract class BinaryTree<K : Comparable<K>, D : Any?, N : Node<K, D?, N>> {
	internal var root: N? = null

	protected fun getMinNode(start: N? = root): N? {
		var resultNode = start ?: return null
		while (true) {
			resultNode = resultNode.left ?: return resultNode
		}
	}
	protected fun getMaxNode(start: N? = root): N? {
		var resultNode = start ?: return null
		while (true) {
			resultNode = resultNode.right ?: return resultNode
		}
	}
	/**
	 * Returns the minimum data in the tree starting from the specified node.
	 *
	 * @param start the node from which to start the search (defaults to the root).
	 * @return the minimum data value, or null if the tree is empty.
	 */
	fun findMin(start: N? = root): D? {
		return getMinNode(start)?.data
	}

	/**
	 * Returns the maximum data in the tree starting from the specified node.
	 *
	 * @param start the node from which to start the search (defaults to the root).
	 * @return the maximum data value, or null if the tree is empty.
	 */
	fun findMax(start: N? = root): D? {
		return getMaxNode(start)?.data
	}

	protected fun findPredecessor(node: N): N? {
		var resultNode = node.left ?: return null
		while (true) {
			resultNode = resultNode.right ?: return resultNode
		}
	}

	/**
	 * Searches for a node with the specified key starting from the given node.
	 *
	 * @param key the key to search for.
	 * @param start the node from which to start the search (defaults to the root).
	 * @return the data associated with the node, or null if no such node is found.
	 */
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

	/**
	 * Inserts a new node with the specified key and data into the tree.
	 * If a node with the same key already exists, its data is updated.
	 *
	 * @param key the key of the node to insert.
	 * @param data the data associated with the key.
	 */
	abstract fun insert(key: K, data: D?)

	/**
	 * Deletes the node with the specified key from the tree.
	 * If the node is not found, an exception is thrown.
	 *
	 * @param key the key of the node to delete.
	 *
	 */
	abstract fun delete(key: K): D?

	/**
	 * Returns an iterator over the data in the tree in in-order traversal.
	 *
	 * @return an iterator over the tree's data.
	 */
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
}
