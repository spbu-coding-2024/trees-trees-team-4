package org.treelib

import kotlin.math.max

class AVLNode<K : Comparable<K>, V : Any>(key: K, data: V) : Node<K, V, AVLNode<K, V>>(key, data) {
	internal var height: Int = 1
}

class AVLTree<K : Comparable<K>, V : Any>(override var root: AVLNode<K, V>? = null) :
	BinaryTree<K, V, AVLNode<K, V>>(root), RotatableTree<K, V, AVLNode<K, V>> {

	private fun getHeight(node: AVLNode<K, V>?): Int {
		return node?.height ?: 0
	}

	private fun updateHeight(node: AVLNode<K, V>) {
		node.height = max(getHeight(node.left), getHeight(node.right))
	}

	private fun getBalance(node: AVLNode<K, V>?): Int {
		return if (node == null) 0 else getHeight(node.left) - getHeight(node.right)
	}

	private fun balance(node: AVLNode<K, V>) {
		val balance = getBalance(node)
		if (balance == 2) {
			if (getBalance(node.right) == -1) node.right?.let { rotateRight(it) }
			rotateLeft(node)
		}
		if (balance == -2) {
			if (getBalance(node.left) == 1) node.left?.let { rotateLeft(it) }
			rotateRight(node)
		}
	}

	override fun rotateLeft(node: AVLNode<K, V>): AVLNode<K, V> {
		val rightChild = node.right ?: return node
		val middleSubtree = rightChild.left

		node.right = middleSubtree
		rightChild.left = node

		node.height = 1 + max(getHeight(node.left), getHeight(node.right))
		rightChild.height = 1 + max(getHeight(rightChild.left), getHeight(rightChild.right))
		return rightChild
	}

	override fun rotateRight(node: AVLNode<K, V>): AVLNode<K, V> {
		val leftChild = node.left ?: return node
		val middleSubtree = leftChild.right

		node.left = middleSubtree
		leftChild.right = node

		node.height = 1 + max(getHeight(node.left), getHeight(node.right))
		leftChild.height = 1 + max(getHeight(leftChild.left), getHeight(leftChild.right))
		return leftChild
	}

	override fun insert(key: K, data: V): AVLNode<K, V>? {
		var insertedNode: AVLNode<K, V>? = null

		fun insertRec(node: AVLNode<K, V>?): AVLNode<K, V>? {
			if (node == null) {
				insertedNode = AVLNode(key, data)
				return insertedNode
			}
			when {
				key < node.key -> node.left = insertRec(node.left)
				key > node.key -> node.right = insertRec(node.right)
				else -> {
					node.data = data
					insertedNode = node
					return node
				}
			}
			node.height = 1 + max(getHeight(node.left), getHeight(node.right))
			val balance = getBalance(node)
			if (balance > 1) {
				if (getBalance(node.left) < 0) node.left = rotateLeft(node.left!!)
				return rotateRight(node)
			}
			if (balance < -1) {
				if (getBalance(node.right) > 0) node.right = rotateRight(node.right!!)
				return rotateLeft(node)
			}
			return node
		}

		root = insertRec(root)
		return insertedNode
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
					val predecessor = findMax(it.left) ?: return null
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
