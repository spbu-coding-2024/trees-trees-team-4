package org.treelib

class AVLNode<K : Comparable<K>, V : Any>(key: K, data: V) : Node<K, V>(key, data) {
	internal var height: Int = 0
}

class AVLTree<K : Comparable<K>, V : Any>(key: K, data: V) : BinaryTree<K, V>(key, data), RotatableTree<K, V> {
	private fun getHeight(node: AVLNode<K, V>?): Int {
		return node?.height ?: -1
	}

	private fun updateHeight(node: AVLNode<K, V>) {
		node.height = Math.max(getHeight(node.left), getHeight(node.right))
	}

	private fun getNodeBalance(node: AVLNode<K, V>?): Int {
		node?.let {
			return getHeight(node?.right) - getHeight(node?.left)
		}
		return 0
	}

	private fun balance(node: AVLNode<K, V>) {
		val balance = getNodeBalance(node)
		if (balance == 2) {
			if (getNodeBalance(node.right) == -1) rotateRight(node.right)
			rotateLeft(node)
		}
		if (balance == -2) {
			if (getNodeBalance(node.left) == -1) rotateRight(node.right)
			rotateLeft(node)
		}
	}

	override fun insert(key: K, value: V): AVLNode<K, V>? {
		root?.let {
			return insert(key, value, it)
		}
		return null
	}

	private fun insert(key: K, value: V, cur: AVLNode<K, V>): AVLNode<K, V>? {
		if (key < cur.key) {
			if (cur.left == null) {
				cur.left = AVLNode(key, value)
				updateHeight(cur)
				balance(cur)
				return cur.left
			} else return insert(key, value, cur.left)
		} else if (key == cur.key) {
			cur.data = value
			return cur
		} else {
			if (cur.right == null) {
				cur.right = AVLNode(key, value)
				updateHeight(cur)
				balance(cur)
				return cur.right
			} else return insert(key, data, cur.right)
		}
	}

	override fun delete(key: K): AVLNode<K, V>? {
		root?.let {
			return delete(key, it)
		}
		return null
	}

	private fun delete(key: K, curNode: AVLNode<K, V>?): AVLNode<K, V>? {
		curNode?.let {
			if (key < it.key)
				return delete(key, it.left)
			if (key > it.key)
				return delete(key, it.right)
			else{
				if (it.left == null || it.right == null){
					return it.left ?: it.right
				}
				else {
					val predecessor = max(it.left)
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
