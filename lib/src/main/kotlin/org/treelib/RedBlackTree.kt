package org.treelib

const val RED = true
const val BLACK = false

class RBNode<K: Comparable<K>, V : Any>(key: K, data: V, colorArg: Boolean): Node<K, V, RBNode<K,V>>(key, data) {
    var color: Boolean = colorArg

    internal fun isRed(): Boolean {
        return this.color == RED
    }
    internal fun leftIsRed() : Boolean {
        return this.left?.color ?: BLACK
    }
    internal fun leftLeftIsRed() : Boolean { //TODO(find null safe solution to not use this method)
        return this.left?.left?.color ?: BLACK
    }
    internal fun rightLeftIsRed() : Boolean {
        return this.right?.left?.color ?: BLACK
    }
    internal fun rightIsRed() : Boolean {
        return this.right?.color ?: BLACK
    }
}

class RedBlackTree<K: Comparable<K>, V : Any>: RotatableTree<K, V, RBNode<K, V>>, BinaryTree<K, V, RBNode<K, V>>() {

    var size = 0

    fun isEmpty(): Boolean {
        return root == null
    }

    private fun flipColors(node: RBNode<K, V>?) {
        node?.let {
            node.color = !node.color
            node.left?.let {
                it.color = !it.color
            }
            node.right?.let {
                it.color = !it.color
            }
        } ?: {
            NullPointerException("Can't flip colors")
        }
    }

    private fun balanceNode(node: RBNode<K, V>): RBNode<K, V> {
        var h = node
        if (!h.leftIsRed() && h.rightIsRed()) {
            h = rotateLeft(h)
        }
        if (h.leftIsRed() && h.leftLeftIsRed()) {
            h =rotateRight(h)
        }
        if (h.leftIsRed() && h.rightIsRed()) {
            flipColors(h)
        }
        return h
    }

    override fun rotateLeft(node: RBNode<K, V>): RBNode<K,V> {
        var x = super.rotateLeft(node)
        x.color = node.color
        node.color = RED
        return x
    }

    override fun rotateRight(node: RBNode<K, V>): RBNode<K,V> {
        var x = super.rotateRight(node)
        x.color = node.color
        node.color = RED
        return x
    }

    override fun min(): RBNode<K,V>? {
        root?.let {
            return min(it)
        } ?:
        throw Exception("Nothing to search")
    }

    private fun min(node: RBNode<K, V>): RBNode<K, V> {
        node.left?.let {
            return min(it)
        } ?: return node
    }

    private fun max(node: RBNode<K, V>): RBNode<K, V> {
        node.right?.let {
            return max(it)
        } ?: return node
    }

    override fun max(): RBNode<K,V>? {
        root?.let {
            return max(it)
        } ?:
        throw Exception("Nothing to search")
    }

    override fun insert(key: K, value: V): RBNode<K, V> {
        var newNode = RBNode<K,V>(key, value, RED)
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

    override fun search(key: K): RBNode<K, V>? {
        root?.let{
            return search(it, key)
        } ?: return null
    }

    private fun search(node: RBNode<K, V>, key: K): RBNode<K, V>? {
        var x: RBNode<K, V>? = null
        if (node.key < key) {
            node.right?.let {
                x = search(it, key)
            }
        }
        else if (node.key > key) {
            node.left?.let {
                x = search(it, key)
            }
        }
        else if (node.key == key) {
            x = node
        }
        return x
    }

    private fun moveRedLeft(node: RBNode<K, V>): RBNode<K, V> {
        require(!node.leftLeftIsRed() && !node.leftIsRed())
        var x = node
        flipColors(x)
        x.right?.let {
            if (it.leftIsRed()) {
                x.right = rotateRight(it)
                x = rotateLeft(x)
                flipColors(x)
            }
        }
        return x
    }

    private fun moveRedRight(node: RBNode<K, V>): RBNode<K, V> {
        require(node.right != null && !node.rightLeftIsRed())
        var x = node
        flipColors(x)
        if (x.leftLeftIsRed()) {
            x = rotateRight(x)
            flipColors(x)
        }
        return x
    }

    fun deleteMax(): RBNode<K, V> {
        root?.let {
            if (!it.rightIsRed() && !it.leftIsRed()) {
                it.color = RED
            }
            var (newRoot, deletedNode)= deleteMax(it)
            root = newRoot
            if (!isEmpty()) it.color = BLACK
            size--
            return deletedNode

        }
        throw NoSuchElementException("deleteMax: Underflow")
    }

    private fun deleteMax(node: RBNode<K, V>?): Pair<RBNode<K, V>?, RBNode<K, V> >{
        var x = node
        x?.let {
            if (x.leftIsRed()) {
                x = rotateRight(x)
            }
            var right = x.right
            if (right == null) {
                return Pair(x.left, node)
            }
            if (!right.isRed() && !right.leftIsRed()) {
                x = moveRedRight(x)
            }
            var (newRight, deletedNode) = deleteMax(x.right)
            x.right = newRight
            return Pair(balanceNode(x), deletedNode)
        } ?: throw Exception("Something went wrong in deleteMax") //TODO(rename this exception)
    }


    fun deleteMin(): RBNode<K,V> {
        root?.let {
            if (!it.rightIsRed() && !it.leftIsRed()) {
                it.color = RED
            }
            var (newRoot, deletedNode) = deleteMin(it)
            root = newRoot
            if (!isEmpty()) root?.color = BLACK
            size--
            return deletedNode
        }
        throw NoSuchElementException("deleteMin: Underflow")
    }

    private fun deleteMin(node: RBNode<K, V>?): Pair<RBNode<K, V>?, RBNode<K, V>> {
        var x = node

        x?.let {
            var left = x.left
            if (left == null) {
                return Pair(null, node)
            }
            if (!left.isRed() && !left.leftIsRed()) {
                x = moveRedLeft(x)
            }

            var (newLeft, deletedNode) = deleteMin(x.left)
            x.left = newLeft

            return Pair(balanceNode(x), deletedNode)
        } ?: throw Exception("Something went wrong in deleteMin") //TODO(rename this exception)
    }

    private fun delete(node: RBNode<K, V>, key: K): RBNode<K, V>? {
        var x = node
        if (key < x.key) {
            if (!x.leftIsRed() && !x.leftLeftIsRed()) {
                x = moveRedLeft(x)
            }
            x.left?.let {
                x.left = delete(it, key)
            } ?: throw Exception("Delete Exception: left node is null")
        } else {
            if (x.leftIsRed()) {
                x = rotateRight(x)
            }
            if (key == x.key && x.right == null) {
                require(x.isRed())
                return null
            }
            if (!x.rightIsRed() && !x.rightLeftIsRed()) {
                x = moveRedRight(x)
            }
            if (key == x.key) {
                x.right?.let {
                    x.data = search(it, min(it).key)?.data ?: throw Exception("Can't find min in subtree")
                    x.key = min(it).key
                    var (newRight, _) = deleteMin(it)
                    x.right = newRight
                }
            }
            else {
                x.right?.let {
                    x.right = delete(it, key)
                } ?: throw Exception("Delete Exception: right node is null")
            }

        }
        return balanceNode(x)
    }

    override fun delete(key: K): RBNode<K, V>? {
        root?.let {
            if (!it.leftIsRed() && !it.rightIsRed()) {
                it.color = RED
            }
            root = delete(it, key)
            if (!isEmpty()) root?.color = BLACK
            return root //TODO("make delete return deleted node")
        }
        throw NoSuchElementException("Nothing to delete")
    }

    override fun iterator(key: K): Iterable<RBNode<K, V>> {
        TODO("WIP")
    }
}
