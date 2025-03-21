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

	private fun insert(key: K, value: V, root: AVLNode<K, V>): AVLNode<K, V>? {
		if (key < root.key) {
			if (root.left == null) {
				root.left = AVLNode(key, value)
				updateHeight(root)
				balance(root)
				return root.left
			} else return insert(key, value, root.left)
		} else if (key == root.key) {
			root.data = value
			return root
		} else {
			if (root.right == null) {
				root.right = AVLNode(key, value)
				updateHeight(root)
				balance(root)
				return root.right
			} else return insert(key, data, root.right)
		}


	}

}
