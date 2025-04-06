package org.treelib

import kotlin.math.max

/**
 * Represents a node in the AVL tree.
 *
 * @param K the type of keys maintained by this node.
 * @param D the type of mapped data.
 * @property key the key associated with this node.
 * @property data the data stored in this node.
 */
class AVLNode<K : Comparable<K>, D : Any?>(key: K, data: D?) : Node<K, D?, AVLNode<K, D?>>(key, data) {
	var height: Int = 1
		private set

	fun updateHeight() {
		height = max(left?.height ?: 0, right?.height ?: 0) + 1
	}
}

/**
 * An AVL tree that maintains balance through rotations.
 *
 * @param K the type of keys maintained by this tree.
 * @param D the type of mapped data.
 * @property root the root node of the AVL tree.
 */
class AVLTree<K : Comparable<K>, D : Any?>(rootKey: K? = null, rootData: D? = null):
	BinaryTree<K, D?, AVLNode<K, D?>>() {

	init {
		if (rootKey != null)
			root = AVLNode(rootKey, rootData)
	}

	enum class Weight(val value: Int) {
		BALANCED(0),
		RIGHT_HEAVY(2),
		LEFT_HEAVY(-2),
		LEFT_RIGHT_HEAVY(1),
		RIGHT_LEFT_HEAVY(-1)
	}

	private fun rotateHelper(node: AVLNode<K, D?>, child: AVLNode<K, D?>) {
		node.updateHeight()
		child.updateHeight()
		if (root == node)
			root = child
	}

	private fun rotateLeft(node: AVLNode<K, D?>): AVLNode<K, D?> {
		val rightChild = node.right ?: return node
		val middleSubtree = rightChild.left

		node.right = middleSubtree
		rightChild.left = node

		rotateHelper(node, rightChild)
		return rightChild
	}

	private fun rotateRight(node: AVLNode<K, D?>): AVLNode<K, D?> {
		val leftChild = node.left ?: return node
		val middleSubtree = leftChild.right

		node.left = middleSubtree
		leftChild.right = node

		rotateHelper(node, leftChild)
		return leftChild
	}

	private fun balance(node: AVLNode<K, D?>): AVLNode<K, D?> {
		fun Int.toWeight(): Weight {
			return Weight.entries.find { it.value == this }
				?: throw IllegalArgumentException("Cannot find weight: $this")
		}

		fun getHeight(node: AVLNode<K, D?>?): Int {
			return node?.height ?: 0
		}

		fun getBalanceFactor(node: AVLNode<K, D?>?): Weight {
			return (getHeight(node?.right) - getHeight(node?.left)).toWeight()
		}

		val nodeBalance = getBalanceFactor(node)
		if (nodeBalance == Weight.RIGHT_HEAVY && getBalanceFactor(node.right) == Weight.RIGHT_LEFT_HEAVY) {
			node.right = rotateRight(
				node.right
					?: throw NoSuchElementException("Cannot find right node in right-left-heavy tree")
			)
		} else if (nodeBalance == Weight.LEFT_HEAVY && getBalanceFactor(node.left) == Weight.LEFT_RIGHT_HEAVY) {
			node.left = rotateLeft(
				node.left
					?: throw NoSuchElementException("Cannot find left node in left-right-heavy tree")
			)
		}

		val rotatedNode = when (nodeBalance) {
			Weight.RIGHT_HEAVY -> rotateLeft(node)
			Weight.LEFT_HEAVY -> rotateRight(node)
			else -> node
		}

		return rotatedNode
	}

	/**
	 * Inserts a new node with the specified [key] and [data] into the AVL tree.
	 *
	 * If a node with the same key already exists, its data is updated.
	 * The tree is rebalanced as needed after insertion.
	 *
	 * @param key the key to insert.
	 * @param data the data associated with the key.
	 */
	override fun insert(key: K, data: D?) {
		fun insertRec(node: AVLNode<K, D?>?): AVLNode<K, D?> {
			if (node == null) {
				return AVLNode(key, data)
			}
			when {
				key < node.key -> node.left = insertRec(node.left)
				key > node.key -> node.right = insertRec(node.right)
				else -> {
					node.data = data
				}
			}
			node.updateHeight()
			return balance(node)
		}
		root = insertRec(root)
	}

	/**
	 * Deletes the node with the specified [key] from the AVL tree.
	 *
	 * The tree is rebalanced after deletion. If the node to delete is not found,
	 * a [NoSuchElementException] is thrown.
	 *
	 * Note that if the node being deleted is the root, the root is updated.
	 * In non-root deletion cases, the function returns the replacement node.
	 *
	 * @param key the key of the node to delete.
	 * @throws NoSuchElementException if a node with the specified key is not found.
	 */
	override fun delete(key: K): D? {
		var deletedData: D? = null
		fun deleteRec(key: K, node: AVLNode<K, D?>?): AVLNode<K, D?>? {
			when {
				node == null -> throw NoSuchElementException("Cannot find node to be deleted: key = $key")
				key < node.key -> node.left = deleteRec(key, node.left)
				key > node.key -> node.right = deleteRec(key, node.right)
				else -> {
					deletedData = node.data
					if (node.left == null || node.right == null) {
						return node.left ?: node.right
					}
					val predecessor = findPredecessor(node)
					node.key = predecessor?.key
						?: throw NoSuchElementException(
							"Cannot find the predecessor of the node to be deleted: key = $key"
						)
					node.data = predecessor.data
					node.left = deleteRec(predecessor.key, node.left)
				}
			}
			node.updateHeight()
			return balance(node)
		}

		if (key == root?.key) {
			root = deleteRec(key, root)
		} else root?.let {
			deleteRec(key, it)
		}
		return deletedData
	}
}
