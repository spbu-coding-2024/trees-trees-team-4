
package org.treelib


abstract class BinaryTree<K: Comparable<K>, V: Any>(var key: K, var data: V) {
    internal var root: Node<K, V>? = null
    fun min(): K? {
        var resultNode = root ?: return null
        while (resultNode.left != null) {
            resultNode = resultNode.left!!
        }
        return resultNode.key
    }

    fun max(): K? {
        var resultNode = root ?: return null
        while (resultNode.right != null) {
            resultNode = resultNode.right!!
        }
        return resultNode.key
    }

    abstract fun insert(key: K, value: V): Node<K, V>?
    abstract fun search(key: K): Node<K, V>?
    abstract fun delete(key: K): Node<K, V>?
    abstract fun iterator(key: K): Iterable<Node<K, V>>
}