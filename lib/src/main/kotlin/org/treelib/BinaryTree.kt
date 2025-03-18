
package org.treelib


abstract class BinaryTree<K: Comparable<K>, V: Any>(var key: K, var data: V) {
    internal var root: Node<K, V>? = null
    abstract fun min(root: Node<K, V>?): K
    abstract fun max(root: Node<K, V>?): K
    abstract fun insert(value: V): Node<K, V>? // what should it return?
    abstract fun search(key: K): Node<K, V>?
    abstract fun delete(key: K): Node<K, V>?
    abstract fun iterator(key: K): Node<K, V>? // what should it return?

}