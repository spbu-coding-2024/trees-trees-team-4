package org.treelib

class RBNode<K: Comparable<K>, V>(var key: K, var data: V): Node {
		var isRed: Boolean = false
				get() = field
				set(bool) { field = bool }
}

class RedBlackTree<K: Comparable<K>, V>: RotatableTree, BinaryTree {
		private var root: RBNode<K, V>? = null

		private fun flipColors(node: RBNode<K, V>?) {
				require(node.left.isRed && node.right.isRed)
				node.isRed = true
				node.left.isRed = false
				node.right.isRed = false
		}

    override fun insert(key: K, value: V): Node<K, V>? {
				val node = super.insert(key, value) //insert must return inserted node
				if (node.right.isRed && !node.left.isRed) {
						node = rotateLeft(node)
				}
				if (node.right.isRed && node.left.left.isRed) {
						node = rotateRight(node)
				}
				if (node.left.isRed && !node.right.isRed) {
						flipColors(node)
				}
				node.isRed = false
				return node
		}
}
