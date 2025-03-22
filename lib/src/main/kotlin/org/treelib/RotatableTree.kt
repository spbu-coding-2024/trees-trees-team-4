package org.treelib

interface RotatableTree<K: Comparable<K>, V : Any, N: Node<K, V, N>> {
  fun rotateLeft(node: N): N {
    val x = node.right
    x?.let {
      node.right = it.left
      it.left = node
      return it
    } ?: let {
      throw NoSuchElementException("Can't rotate left: right node is null")
    }
  }
  fun rotateRight(node: N): N {
    val x = node.left
    x?.let {
      node.left = x.right
      x.right = node
      return x
    } ?: let {
      throw NoSuchElementException("Can't rotate right: left node is null")
    }

  }
}
