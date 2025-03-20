package org.treelib

interface RotatableTree<K: Comparable<K>, V : Any> {
  fun rotateLeft(node: Node<K, V>): Node<K, V> {
    val x = node.right
    x?.let {
      node.right = x.left
      x.left = node
      return x
    } ?: let {
      throw NullPointerException("Can't rotate left: right node is null")
    }
  }
  fun rotateRight(node: Node<K, V>): Node<K, V> {
    val x = node.left
    x?.let {
      node.left = x.right
      x.right = node
      return x
    } ?: let {
      throw NullPointerException("Can't rotate right: left node is null")
    }

  }
}
