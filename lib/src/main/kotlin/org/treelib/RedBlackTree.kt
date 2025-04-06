package org.treelib

const val RED = true
const val BLACK = false

/**
 * A Node of Red-Black Tree
 * @param K the key type
 * @param V the value type
 * @param key a key associated with the tree
 * @param data a data value associated with the tree
 */
class RBNode<K: Comparable<K>, V : Any>(key: K, data: V): Node<K, V, RBNode<K,V>>(key, data) {
    /**
     * A color of a node.
     */
    var color: Boolean = RED
}
/**
 * A Red-Black tree then balances itself
 * @param K the key type
 * @param V the value type
 * @property root the root of a tree
 */
class RedBlackTree<K: Comparable<K>, V : Any>: BinaryTree<K, V, RBNode<K, V>>() {

    /**
     * The amount of node in tree.
     */

    private fun isRed(node: RBNode<K, V>?): Boolean {
        return node?.color ?: BLACK
    }

    private fun flipColors(node: RBNode<K, V>?) {
        checkNotNull(node)
        node.color = !node.color
        node.left?.let {
            it.color = !it.color
        }
        node.right?.let {
            it.color = !it.color
        }
    }

    private fun balanceNode(node: RBNode<K, V>): RBNode<K, V> {
        var current = node

        if (!isRed(current.left) && isRed(current.right)) {
            current = rotateLeft(current)
        }
        if (isRed(current.left) && isRed(current.left?.left)) {
            current = rotateRight(current)
        }
        if (isRed(current.left) && isRed(current.right)) {
            flipColors(current)
        }
        return current
    }

    private fun rotateLeft(node: RBNode<K, V>): RBNode<K,V> {
        val right = node.right
        right?.let {
            node.right = it.left
            it.left = node
            it.color = node.color
            node.color = RED
            return it
        } ?: let {
            throw NoSuchElementException("Can't rotate left: right node is null")
        }
    }

    private fun rotateRight(node: RBNode<K, V>): RBNode<K,V> {
        val left = node.left
        left?.let {
            node.left = left.right
            left.right = node
            left.color = node.color
            node.color = RED
            return left
        } ?: let {
            throw NoSuchElementException("Can't rotate right: left node is null")
        }
    }

    /**
     * Inserts value with corresponding key.

     * If there's no node with the same key in tree, then create new node.
     *
     * If there's node with same key, then overwrite value of this node.
     *
     * @param key a key of a node to be inserted
     * @param value a value of a node to be inserted
     * @return the inserted node
     */
    override fun insert(key: K, value: V): RBNode<K, V> {
        var newNode = RBNode<K,V>(key, value)
        root = insert(root, key, value, newNode)
        root?.color = BLACK
        return newNode
    }

    private fun insert(node: RBNode<K, V>?, key: K, value: V, newNode: RBNode<K, V>): RBNode<K, V> {
        if (node == null) {
            return newNode
        }
        when {
            (node.key > key) -> node.left = insert(node.left, key, value, newNode)
            (node.key < key) -> node.right = insert(node.right, key, value, newNode)
            else ->node.data = value
        }
        return balanceNode(node)
    }

    private fun moveRedLeft(node: RBNode<K, V>): RBNode<K, V> {
        require(isRed(node) && !isRed(node.left) && !isRed(node.left?.left))
        var current = node
        flipColors(current)
        current.right?.let {
            if (isRed(it.left)) {
                current.right = rotateRight(it)
                current = rotateLeft(current)
                flipColors(current)
            }
        }
        return current
    }

    private fun moveRedRight(node: RBNode<K, V>): RBNode<K, V> {
        require(isRed(node) && !isRed(node.right) && !isRed(node.right?.left))
        var current = node
        flipColors(current)
        if (isRed(current.left?.left)) {
            current = rotateRight(current)
            flipColors(current)
        }
        return current
    }
    /**
     * Deletes the minimal element in tree

     * @return the deleted node
     * @throws NoSuchElementException if there's no nodes in tree
     */
    fun deleteMin(): RBNode<K,V> {
        root?.let {
            if (!isRed(it.left) && !isRed(it.right)) {
                it.color = RED
            }
            var (newRoot, deletedNode) = deleteMin(it)
            root = newRoot
            if (root != null) root?.color = BLACK
            return deletedNode
        } ?:
        throw NoSuchElementException("Nothing to delete")
    }

    private fun deleteMin(node: RBNode<K, V>): Pair<RBNode<K, V>?, RBNode<K, V>> {
        var current = node

         if (current.left == null) {
            return Pair(null, node)
        }
        if (!isRed(current.left) && !isRed(current.left?.left)) {
            current = moveRedLeft(current)
        }
        current.left?.let {
            var (newLeft, deletedNode) = deleteMin(it)
            current.left = newLeft
            return Pair(balanceNode(current), deletedNode)
        } ?: error("deleteMin: left node is null")
    }

    private fun delete(node: RBNode<K, V>, key: K): Pair<RBNode<K, V>?,RBNode<K, V>?> {
        var current = node
        var deletedNode: RBNode<K, V>? = null
        if (key < current.key) {
            if (!isRed(current.left) && !isRed(current.left?.left)) {
                current = moveRedLeft(current)
            }
            current.left?.let{
                var returnValue = delete(it, key)
                current.left = returnValue.first
                deletedNode = returnValue.second
            }
        } else {
            if (isRed(current.left)) {
                current = rotateRight(current)
            }
            if (key == current.key && current.right == null) {
                require(isRed(current))
                return Pair(null, node)
            }
            if (!isRed(current.right) && !isRed(current.right?.left)) {
                current = moveRedRight(current)
            }
            if (key == current.key) {
                val minNode = findMin(current.right)
                minNode?.let {
                    current.key = it.key
                    current.data = it.data
                } ?: throw error("delete: findMin returns null")
                current.right?.let {
                    var returnValue = deleteMin(it)
                    current.right = returnValue.first
                    deletedNode = returnValue.second
                }
            }
            else {
                current.right?.let {
                    var returnValue = delete(it, key)
                    current.right = returnValue.first
                    deletedNode = returnValue.second
                }
            }
        }
        return Pair(balanceNode(current), deletedNode)
    }

    /**
    * Deletes an element with the given key in tree
    *
    * @param key a key of node to be deleted
    * @return the deleted node
    * @throws NoSuchElementException if there's no nodes in tree
    */
    override fun delete(key: K): RBNode<K, V>? {
        root?.let {
            if (search(key) == null) return null
            if (!isRed(it.left) && !isRed(it.right)) {
                it.color = RED
            }
            var (newRoot, deletedNode) = delete(it, key)
            root = newRoot
            if (root != null) root?.color = BLACK
            return deletedNode
        } ?:
        throw NoSuchElementException("Nothing to delete")
    }
}
