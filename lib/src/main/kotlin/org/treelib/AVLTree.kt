package org.treelib

import kotlin.math.max

typealias NSEE = NoSuchElementException

const val RIGHT_HEAVY = 2
const val LEFT_HEAVY = -2
const val LEFT_RIGHT_HEAVY = 1
const val RIGHT_LEFT_HEAVY = -1


class AVLNode<K : Comparable<K>, V : Any>(key: K, data: V) : Node<K, V, AVLNode<K, V>>(key, data) {
	internal var height: Int = 1
}

class AVLTree<K : Comparable<K>, V : Any>(override var root: AVLNode<K, V>? = null) :
	BinaryTree<K, V, AVLNode<K, V>>(root), RotatableTree<K, V, AVLNode<K, V>> {

	private fun getHeight(node: AVLNode<K, V>?): Int {
		return node?.height ?: 0
	}

	private fun updateHeight(node: AVLNode<K, V>) {
		node.height = max(getHeight(node.left), getHeight(node.right)) + 1
	}

	private fun getBalance(node: AVLNode<K, V>?): Int {
		return if (node == null) 0 else getHeight(node.right) - getHeight(node.left)
	}

	private fun balance(node: AVLNode<K, V>): AVLNode<K, V> {
		val balance = getBalance(node)
		if (balance == RIGHT_HEAVY) {
			if (getBalance(node.right) == RIGHT_LEFT_HEAVY) node.right =
				rotateRight(node.right ?: throw NSEE())
			return rotateLeft(node)
		}
		if (balance == LEFT_HEAVY) {
			if (getBalance(node.left) == LEFT_RIGHT_HEAVY) node.left =
				rotateLeft(node.left ?: throw NSEE())
			return rotateRight(node)
		}
		return node
	}

	override fun rotateLeft(node: AVLNode<K, V>): AVLNode<K, V> {
		val rightChild = node.right ?: return node
		val middleSubtree = rightChild.left

		node.right = middleSubtree
		rightChild.left = node

		updateHeight(node)
		updateHeight(rightChild)

		if (root == node) root = rightChild

		return rightChild
	}

	override fun rotateRight(node: AVLNode<K, V>): AVLNode<K, V> {
		val leftChild = node.left ?: return node
		val middleSubtree = leftChild.right

		node.left = middleSubtree
		leftChild.right = node

		updateHeight(node)
		updateHeight(leftChild)

		if (root == node) root = leftChild

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
			updateHeight(node)
			return balance(node)
		}

		root = insertRec(root)
		return insertedNode
	}

	override fun delete(key: K): AVLNode<K, V>? {
		var swappedNode: AVLNode<K, V>? = null
		fun deleteRec(key: K, node: AVLNode<K, V>?): AVLNode<K, V>? {
			when {
				node == null -> throw NSEE()
				key < node.key -> node.left = deleteRec(key, node.left)
				key > node.key -> node.right = deleteRec(key, node.right)
				else -> {
					if (node.left == null || node.right == null) {
						swappedNode = node.left ?: node.right
						return swappedNode
					}
					val temp = findMax(node.left)
					node.key = temp?.key ?: throw NSEE()
					node.data = temp.data
					swappedNode = node
					node.left = deleteRec(temp.key, node.left)
				}
			}
			updateHeight(node)
			return balance(node)
		}

		if (key == root?.key) {
			root = deleteRec(key, root)
		} else root?.let {
			deleteRec(key, it)
			return swappedNode
		}
		return root
	}
}
