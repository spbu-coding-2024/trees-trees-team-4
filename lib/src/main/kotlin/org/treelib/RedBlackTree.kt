package org.treelib

const val RED = true
const val BLACK = false

/**
 * A Node of Red-Black Tree
 * @param K the key type
 * @param D the data value type
 * @param key a key associated with the tree
 * @param data a data value associated with the tree
 */
class RBNode<K: Comparable<K>, D : Any?>(key: K, data: D?): Node<K, D?, RBNode<K,D?>>(key, data) {
    /**
     * A color of a node.
     */
    var color: Boolean = RED
}
/**
 * A Red-Black tree then balances itself
 * @param K the key type
 * @param D the data value type
 * @property root the root of a tree
 */
class RedBlackTree<K: Comparable<K>, D : Any?>(rootKey: K? = null, rootData: D? = null):
    BinaryTree<K, D?, RBNode<K, D?>>() {

    init {
        if (rootKey != null)
            root = RBNode(rootKey, rootData)
    }

    private fun isRed(node: RBNode<K, D?>?): Boolean {
        return node?.color ?: BLACK
    }

    private fun flipColors(node: RBNode<K, D?>?) {
        checkNotNull(node)
        node.color = !node.color
        node.left?.let {
            it.color = !it.color
        }
        node.right?.let {
            it.color = !it.color
        }
    }

    private fun balanceNode(node: RBNode<K, D?>): RBNode<K, D?> {
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

    private fun rotateLeft(node: RBNode<K, D?>): RBNode<K, D?> {
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

    private fun rotateRight(node: RBNode<K, D?>): RBNode<K, D?> {
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
     */
    override fun insert(key: K, value: D?) {
        var newNode = RBNode<K, D?>(key, value)
        root = insert(root, key, value, newNode)
        root?.color = BLACK
    }

    private fun insert(node: RBNode<K, D?>?, key: K, value: D?, newNode: RBNode<K, D?>): RBNode<K, D?> {
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

    private fun moveRedLeft(node: RBNode<K, D?>): RBNode<K, D?> {
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

    private fun moveRedRight(node: RBNode<K, D?>): RBNode<K, D?> {
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

     * @return the deleted data value
     * @throws NoSuchElementException if there's no nodes in tree
     */
    fun deleteMin(): D? {
        root?.let {
            if (!isRed(it.left) && !isRed(it.right)) {
                it.color = RED
            }
            var (newRoot, deletedData) = deleteMin(it)
            root = newRoot
            if (root != null) root?.color = BLACK
            return deletedData
        } ?:
        throw NoSuchElementException("Nothing to delete")
    }

    private fun deleteMin(node: RBNode<K, D?>): Pair<RBNode<K, D?>?, D?> {
        var current = node

         if (current.left == null) {
            return Pair(null, node.data)
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

    private fun delete(node: RBNode<K, D?>, key: K): Pair<RBNode<K, D?>?,D?> {
        var current = node
        var deletedData: D? = null
        if (key < current.key) {
            if (!isRed(current.left) && !isRed(current.left?.left)) {
                current = moveRedLeft(current)
            }
            current.left?.let{
                var returnValue = delete(it, key)
                current.left = returnValue.first
                deletedData = returnValue.second
            }
        } else {
            if (isRed(current.left)) {
                current = rotateRight(current)
            }
            if (key == current.key && current.right == null) {
                require(isRed(current))
                return Pair(null, node.data)
            }
            if (!isRed(current.right) && !isRed(current.right?.left)) {
                current = moveRedRight(current)
            }
            if (key == current.key) {
                val minNode = getMinNode(current.right)
                minNode?.let {
                    current.key = it.key
                    current.data = it.data
                } ?: throw error("delete: findMin returns null")
                current.right?.let {
                    var returnValue = deleteMin(it)
                    current.right = returnValue.first
                    deletedData = returnValue.second
                }
            }
            else {
                current.right?.let {
                    var returnValue = delete(it, key)
                    current.right = returnValue.first
                    deletedData = returnValue.second
                }
            }
        }
        return Pair(balanceNode(current), deletedData)
    }

    /**
    * Deletes an element with the given key in tree
    *
    * @param key a key of node to be deleted
    * @return the deleted data value
    * @throws NoSuchElementException if there's node to be deleted or tree is empty
    */
    override fun delete(key: K): D? {
        root?.let {
            if (search(key) == null) throw NoSuchElementException("No such node to be deleted")
            if (!isRed(it.left) && !isRed(it.right)) {
                it.color = RED
            }
            var (newRoot, deletedNode) = delete(it, key)
            root = newRoot
            if (root != null) root?.color = BLACK
            return deletedNode
        } ?:
        throw NoSuchElementException("Tree is empty")
    }
}
