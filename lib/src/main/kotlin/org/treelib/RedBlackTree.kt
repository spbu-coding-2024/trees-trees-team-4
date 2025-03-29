package org.treelib

const val RED = true
const val BLACK = false

class RBNode<K: Comparable<K>, V : Any>(key: K, data: V): Node<K, V, RBNode<K,V>>(key, data) {
    var color: Boolean = RED
}

class RedBlackTree<K: Comparable<K>, V : Any>: RotatableTree<K, V, RBNode<K, V>>, BinaryTree<K, V, RBNode<K, V>>() {
    var size = 0
        private set

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
        var x = node
        if (!isRed(x.left) && isRed(x.right)) {
            x = rotateLeft(x)
        }
        if (isRed(x.left) && isRed(x.left?.left)) {
            x = rotateRight(x)
        }
        if (isRed(x.left) && isRed(x.right)) {
            flipColors(x)
        }
        return x
    }

    override fun rotateLeft(node: RBNode<K, V>): RBNode<K,V> {
        val x = node.right
        x?.let {
            node.right = x.left
            x.left = node
            x.color = node.color
            node.color = RED
            return x
        } ?: let {
            throw NoSuchElementException("Can't rotate left: right node is null")
        }
    }

    override fun rotateRight(node: RBNode<K, V>): RBNode<K,V> {
        val x = node.left
        x?.let {
            node.left = x.right
            x.right = node
            x.color = node.color
            node.color = RED
            return x
        } ?: let {
            throw NoSuchElementException("Can't rotate right: left node is null")
        }
    }

    override fun insert(key: K, value: V): RBNode<K, V> {
        var newNode = RBNode<K,V>(key, value)
        root = insert(root, key, value, newNode)
        root?.color = BLACK
        size++
        return newNode
    }

    private fun insert(node: RBNode<K, V>?, key: K, value: V, newNode: RBNode<K, V>): RBNode<K, V> {
        if (node == null) {
            return newNode
        }
        if (node.key > key) {
            node.left = insert(node.left, key, value, newNode)
        }
        else if (node.key < key) {
            node.right = insert(node.right, key, value, newNode)
        }
        else {
            node.data = value
        }

        return balanceNode(node)
    }

    private fun moveRedLeft(node: RBNode<K, V>): RBNode<K, V> {
        require(isRed(node) && !isRed(node.left) && !isRed(node.left?.left))
        var x = node
        flipColors(x)
        x.right?.let {
            if (isRed(it.left)) {
                x.right = rotateRight(it)
                x = rotateLeft(x)
                flipColors(x)
            }
        }
        return x
    }

    private fun moveRedRight(node: RBNode<K, V>): RBNode<K, V> {
        require(isRed(node) && !isRed(node.right) && !isRed(node.right?.left))
        var x = node
        flipColors(x)
        if (isRed(x.left?.left)) {
            x = rotateRight(x)
            flipColors(x)
        }
        return x
    }

    fun deleteMin(): RBNode<K,V> {
        root?.let {
            if (!isRed(it.left) && !isRed(it.right)) {
                it.color = RED
            }
            var (newRoot, deletedNode) = deleteMin(it)
            root = newRoot
            if (root != null) root?.color = BLACK
            size--
            return deletedNode
        } ?:
        throw NoSuchElementException("Nothing to delete")
    }

    private fun deleteMin(node: RBNode<K, V>): Pair<RBNode<K, V>?, RBNode<K, V>> {
        var x = node

        if (x.left == null) {
            return Pair(null, node)
        }
        if (!isRed(x.left) && !isRed(x.left?.left)) {
            x = moveRedLeft(x)
        }
        x.left?.let {
            var (newLeft, deletedNode) = deleteMin(it)
            x.left = newLeft
            return Pair(balanceNode(x), deletedNode)
        } ?: error("deleteMin: left node is null")
    }

    private fun delete(node: RBNode<K, V>, key: K): Pair<RBNode<K, V>?,RBNode<K, V>?> {
        var x = node
        var deletedNode: RBNode<K, V>? = null
        if (key < x.key) {
            if (!isRed(x.left) && !isRed(x.left?.left)) {
                x = moveRedLeft(x)
            }
            x.left?.let{
                var returnValue = delete(it, key)
                x.left = returnValue.first
                deletedNode = returnValue.second
            }
        } else {
            if (isRed(x.left)) {
                x = rotateRight(x)
            }
            if (key == x.key && x.right == null) {
                require(isRed(x))
                return Pair(null, node)
            }
            if (!isRed(x.right) && !isRed(x.right?.left)) {
                x = moveRedRight(x)
            }
            if (key == x.key) {
                val minNode = findMin(x.right)
                minNode?.let {
                    x.key = it.key
                    x.data = it.data
                } ?: throw error("delete: findMin returns null")
                x.right?.let {
                    var returnValue = deleteMin(it)
                    x.right = returnValue.first
                    deletedNode = returnValue.second
                }
            }
            else {
                x.right?.let {
                    var returnValue = delete(it, key)
                    x.right = returnValue.first
                    deletedNode = returnValue.second
                }
            }
        }
        return Pair(balanceNode(x), deletedNode)
    }

    override fun delete(key: K): RBNode<K, V>? {
        root?.let {
            if (search(key) == null) return null
            if (!isRed(it.left) && !isRed(it.right)) {
                it.color = RED
            }
            var (newRoot, deletedNode) = delete(it, key)
            root = newRoot
            if (root != null) root?.color = BLACK
            size--
            return deletedNode
        } ?:
        throw NoSuchElementException("Nothing to delete")
    }
}
