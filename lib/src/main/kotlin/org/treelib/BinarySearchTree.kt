package org.treelib

/**
 * Represents a node in the binary search tree.
 *
 * @param K the type of keys maintained by this node.
 * @param D the type of mapped values.
 * @property key the key associated with this node.
 * @property data the value stored in this node.
 */
class BSTNode<K : Comparable<K>, D : Any?>(key: K, data: D?) : Node<K, D?, BSTNode<K, D?>>(key, data)

/**
 * Binary search tree that stores key-value pairs in a sorted order, allowing efficient search, insertion, and deletion.
 *
 * @param K the type of keys maintained by this tree. Keys must be comparable.
 * @param D the type of mapped values.
 * @property root the root node of the binary search tree, or null if the tree is empty.
 */
class BinarySearchTree<K : Comparable<K>, D : Any>(rootKey: K? = null, rootData: D? = null) :
    BinaryTree<K, D?, BSTNode<K, D?>>() {

    init {
        if (rootKey != null) root = BSTNode(rootKey, rootData)
    }

    /**
     * Inserts a new node with the specified [key] and [data] into the binary search tree.
     *
     * If a node with the same key already exists, its data is updated.
     *
     * @param key a key of a node to be inserted
     * @param data a value of a node to be inserted
     */
    override fun insert(key: K, data: D?) {
        val resultNode: BSTNode<K, D?> = BSTNode(key, data)
        var parent: BSTNode<K, D?>? = null
        var currentNode: BSTNode<K, D?>? = root
        while (currentNode != null && currentNode.key != key) {
            parent = currentNode
            currentNode = stepDeep(currentNode, key)
        }
        when {
            parent == null -> root = replaceByUpdating(root, resultNode)
            parent.key > resultNode.key -> parent.left = replaceByUpdating(parent.left, resultNode)
            else -> parent.right = replaceByUpdating(parent.right, resultNode)
        }
    }

    /**
     * Deletes the node with the specified [key] from the binary search tree.
     *
     * @param key the key of the node to be deleted.
     * @return the data of deleted node
     * @throws NoSuchElementException, if impossible to find node with key passed
     */
    override fun delete(key: K): D? {
        var result: BSTNode<K, D?>? = root
        var target: BSTNode<K, D?>? = root
        var current: BSTNode<K, D?>? = null
        while (target != null) {
            // If the target node has been found
            if (target.key == key) {
                result = target
                when {
                    current == null -> root = replaceWithAppending(target, getMinNode(target.right))
                    current.left == target -> current.left = replaceWithAppending(target, getMinNode(target.right))
                    else -> current.right = replaceWithAppending(target, getMinNode(target.right))
                }
                break
            }
            current = target
            target = if (target.key > key) target.left else target.right
        }
        if (result == null) {
            throw NoSuchElementException("Cannot find node to be deleted")
        } else {
            return result.data
        }
    }

    /**
     * Recursively goes around the whole tree and checks binary search tree invariant
     *
     * @param root the node of subtree to be checked.
     * @return result of checking invariant - true or false
     */
    fun isConsistent(root: BSTNode<K, D?>? = this.root): Boolean {
        var result = false
        val left: BSTNode<K, D?>? = root?.left
        val right: BSTNode<K, D?>? = root?.right
        if (root == null) result = true
        else if (left == null && right == null) {
            result = true
        } else if (right != null && left != null) {
            result = left.key < root.key && root.key < right.key && isConsistent(right) && isConsistent(left)
        } else if (right != null) {
            result = root.key < right.key && isConsistent(right)
        } else if (left != null) {
            result = root.key > left.key && isConsistent(left)
        }
        return result
    }

    private fun replaceWithAppending(target: BSTNode<K, D?>, required: BSTNode<K, D?>?): BSTNode<K, D?>? {
        val result: BSTNode<K, D?>? = when {
            target.right == null -> target.left
            target.left == null -> target.right
            required != null -> {
                required.left = target.left
                target.right
            }

            else -> target.right
        }
        return result
    }

    private fun replaceByUpdating(target: BSTNode<K, D?>?, required: BSTNode<K, D?>): BSTNode<K, D?> {
        if (target?.key == required.key) {
            target.data = required.data
            return target
        } else {
            return required
        }
    }

    private fun stepDeep(node: BSTNode<K, D?>, key: K): BSTNode<K, D?>? {
        val result: BSTNode<K, D?>? = when {
            node.key > key -> node.left
            node.key < key -> node.right
            else -> node
        }

        return result
    }
}
