package org.treelib

class BST<K: Comparable<K>, V: Any>(key: K, data: V) : BinaryTree<K, V>(key, data) {
    override fun min(root: Node<K, V>?): K {
        TODO("Not yet implemented")
    }

    override fun max(root: Node<K, V>?): K {
        TODO("Not yet implemented")
    }

    override fun insert(key: K, value: V): Node<K, V>? {
        TODO("Not yet implemented")
    }

    override fun search(key: K): Node<K, V>? {
        TODO("Not yet implemented")
    }

    override fun delete(key: K): Node<K, V>? {
        TODO("Not yet implemented")
    }

    override fun iterator(key: K): Iterable<Node<K, V>> {
        TODO("Not yet implemented")
    }
}