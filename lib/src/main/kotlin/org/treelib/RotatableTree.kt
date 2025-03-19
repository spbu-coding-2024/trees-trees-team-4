package org.treelib

interface RotatableTree<K: Comparable<K>, V> {
	fun rotateLeft(node: Node<K, V>)
	fun rotateRight(node: Node<K, V>)
}