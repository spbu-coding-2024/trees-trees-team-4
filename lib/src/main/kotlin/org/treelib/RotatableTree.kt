package org.treelib

interface RotatableTree<K: Comparable<K>, V : Any, N: Node<K, V, N>> {
  fun rotateLeft(node: N): N
  fun rotateRight(node: N): N
}
