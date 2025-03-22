package org.treelib

class BSTNode<K: Comparable<K>, V: Any>(key: K, data: V) : Node<K, V>(key, data)

class BST<K: Comparable<K>, V: Any>(key: K, data: V) : BinaryTree<K, V>(key, data) {

    override var root: Node<K, V>? = BSTNode(key, data)

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