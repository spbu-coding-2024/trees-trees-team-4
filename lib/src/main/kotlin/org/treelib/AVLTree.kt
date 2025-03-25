package org.treelib

class AVLNode<K : Comparable<K>, V : Any>(key: K, data: V) : Node<K, V, AVLNode<K, V>>(key, data) {
	internal var height: Int = 0
}

class AVLTree<K : Comparable<K>, V : Any>(key: K, data: V) : BinaryTree<K, V, AVLNode<K, V>>(key, data),
	RotatableTree<K, V, AVLNode<K, V>> {
	override var root: AVLNode<K, V>? = AVLNode(key, data)

	private fun getHeight(node: AVLNode<K, V>?): Int {
		return node?.height ?: -1
	}

	private fun updateHeight(node: AVLNode<K, V>) {
		node.height = Math.max(getHeight(node.left), getHeight(node.right))
	}

	private fun getNodeBalance(node: AVLNode<K, V>?): Int {
		node?.let {
			return getHeight(node.right) - getHeight(node.left)
		}
		return 0
	}

	private fun balance(node: AVLNode<K, V>) {
		val balance = getNodeBalance(node)
		if (balance == 2) {
			if (getNodeBalance(node.right) == -1) node.right?.let { rotateRight(it) }
			rotateLeft(node)
		}
		if (balance == -2) {
			if (getNodeBalance(node.left) == 1) node.left?.let { rotateLeft(it) }
			rotateRight(node)
		}
	}

	override fun insert(key: K, data: V): AVLNode<K, V>? {
		root?.let {
			return insert(key, data, it as AVLNode<K, V>)
		}
		return null
	}

	private fun insert(key: K, data: V, cur: AVLNode<K, V>): AVLNode<K, V> {
		if (key < cur.key) {
			val leftChild = cur.left
			leftChild?.let {
				return insert(key, data, leftChild)
			} ?: run {
				val newNode = AVLNode(key, data)
				cur.left = newNode
				updateHeight(cur)
				balance(cur)
				return newNode
			}
		} else if (key > cur.key) {
			val rightChild = cur.right
			rightChild?.let {
				return insert(key, data, rightChild)
			} ?: run {
				val newNode = AVLNode(key, data)
				cur.right = newNode
				updateHeight(cur)
				balance(cur)
				return newNode
			}
		} else {
			cur.data = data
			return cur
		}
	}

	override fun delete(key: K): AVLNode<K, V>? {
		if (key == root?.key) {
			root = delete(key, root)
		} else root?.let {
			return delete(key, it)
		}
		return root
	}

	private fun delete(key: K, cur: AVLNode<K, V>?): AVLNode<K, V>? {
		cur?.let {
			if (key < it.key) return delete(key, it.left)
			if (key > it.key) return delete(key, it.right)
			else {
				if (it.left == null || it.right == null) {
					return it.left ?: it.right
				} else {
					val predecessor = max(it.left) ?: return null
					it.key = predecessor.key
					it.data = predecessor.data
					it.left = delete(predecessor.key, it.left)
				}
			}
			updateHeight(it)
			balance(it)
			return it
		}
		return null
	}
}
