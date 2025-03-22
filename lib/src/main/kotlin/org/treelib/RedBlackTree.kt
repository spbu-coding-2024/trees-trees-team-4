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
    internal fun leftLeftIsRed() : Boolean {
        return this.left?.left?.color ?: BLACK
    }
    internal fun rightIsRed() : Boolean {
        return this.right?.color ?: BLACK
    }
}

class RedBlackTree<K: Comparable<K>, V : Any>: RotatableTree<K, V, RBNode<K, V>>, BinaryTree<K, V, RBNode<K, V>>() {

    var size = 0

    private fun flipColors(node: RBNode<K, V>?) {
        node?. let {
            require(node.leftIsRed() && node.rightIsRed())
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
        var current = root
        while (current?.left != null) {
            current = current.left
        }
        return current
    }

    override fun max(): RBNode<K, V>? {
        var current = root
        while (current?.right != null) {
            current = current.right
        }
        return current
    }

    override fun insert(key: K, value: V) {
        val node = insert(root, key, value)
        node.color = BLACK
        size++
        root = node
    }

    private fun insert(node: RBNode<K, V>?, key: K, value: V): RBNode<K, V> {
        if (node == null) {
            return RBNode(key, value, RED)
        }
        if (node.key > key) {
            node.left = insert(node.left, key, value)
        }
        else if (node.key < key) {
            node.right = insert(node.right, key, value)
        }
        else {
            node.data = value
        }

        return balanceNode(node)
    }

    override fun search(key: K): RBNode<K, V>? {
        var current = root
        while (current != null) {
            if (current.key < key) {
                current = current.right
            }
            else if (current.key > key) {
                current = current.left
            }
            else if (current.key == key) {
                return current
            }
        }
        return null
    }

    private fun moveRedLeft(node: RBNode<K, V>): RBNode<K, V> {
        var x = node
        flipColors(x)
        //if (x.right != null && node.right?.left != null) {
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

//    fun deleteMax() {
//        root?.let {
//            if (!it.rightIsRed() && !it.leftIsRed()) {
//                it.color = RED
//            }
//            root = deleteMax(it)
//            if (root != null) it.color = BLACK
//        } ?: {
//            throw NoSuchElementException("deleteMax: Underflow")
//        }
//
//    }
//
//    private fun deleteMax(node: RBNode<K, V>?): RBNode<K, V>? {
//        var x = node
//        x?.let {
//            if (!x.leftIsRed() && !x.rightIsRed()) {
//                x = moveRedLeft(x)
//            }
//            x = deleteMax(x.left)
//
//            x?.let {
//                return balanceNode(it)
//            } ?: throw Exception("Something went wrong while deleting")
//        }
//        return throw Exception("Something went wrong while deleting")
//    }


    fun deleteMin() {
        root?.let {
            if (!it.rightIsRed() && !it.leftIsRed()) {
                it.color = RED
            }
            root = deleteMin(it)
            if (root != null) it.color = BLACK
        } ?: {
            throw NoSuchElementException("deleteMin: Underflow")
        }

    }

    private fun deleteMin(node: RBNode<K, V>?): RBNode<K, V>? {
        var x = node
        x?.left?.let {
            if (!it.isRed() && !it.leftIsRed()) {
                x = moveRedLeft(x)
            }
            x = deleteMin(x.left)

            x?.let {
                return balanceNode(it)
            } ?: throw Exception("Something went wrong while deleting")
        }
        return throw Exception("Something went wrong while deleting")
    }

    override fun delete(key: K): RBNode<K, V>? {
        TODO("WIP")
    }

    override fun iterator(key: K): Iterable<RBNode<K, V>> {
        TODO("WIP")
    }

}
