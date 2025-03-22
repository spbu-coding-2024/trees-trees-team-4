
package org.treelib


abstract class BinaryTree<K: Comparable<K>, V: Any>(var key: K, var data: V) {
    internal abstract var root: Node<K, V>?
    fun min(): Node<K, V>? {
        var resultNode = root ?: return null
        while (true) {
            resultNode = resultNode.left ?: break
        }
        return resultNode
    }

    fun max(): Node<K, V>? {
        var resultNode = root ?: return null
        while (true) {
            resultNode = resultNode.right ?: break
        }
        return resultNode
    }

    abstract fun insert(key: K, value: V): Node<K, V>?
    abstract fun search(key: K): Node<K, V>?
    abstract fun delete(key: K): Node<K, V>?
    abstract fun iterator(key: K): Iterable<Node<K, V>>
}