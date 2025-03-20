package org.treelib

interface RotatableTree<K: Comparable<K>, V: Any> {
	fun rotateLeft(node: Node<K, V>)
	fun rotateRight(node: Node<K, V>)
}