package org.treelib

interface RotatableTree<K: Comparable<K>, V> {
  fun rotateLeft(node: Node<K, V>) {
    requireNotNull(node.right)
    val x = node.right
    node.right = x.left
    x.left = node
    return x
  }

  fun rotateRight(node: Node<K, V>) {
    requireNotNull(node.left)
    val x = node.left
    node.left = x.right
    x.right = node
    return x
  }
}
