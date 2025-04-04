package org.treelib

/**
 * Represents a node in the binary search tree.
 *
 * @param K the type of keys maintained by this node.
 * @param V the type of mapped values.
 * @property key the key associated with this node.
 * @property data the value stored in this node.
 */
class BSTNode<K : Comparable<K>, V : Any>(key: K, data: V) : Node<K, V, BSTNode<K, V>>(key, data)

/**
 * Binary search tree that stores key-value pairs in a sorted order, allowing efficient search, insertion, and deletion.
 *
 * @param K the type of keys maintained by this tree. Keys must be comparable.
 * @param V the type of mapped values.
 * @property root the root node of the binary search tree, or null if the tree is empty.
 */
class BinarySearchTree<K : Comparable<K>, V : Any>(override var root: BSTNode<K, V>? = null) :
    BinaryTree<K, V, BSTNode<K, V>>(root) {

    /**
     * Inserts a new node with the specified [key] and [data] into the binary search tree.
     *
     * If a node with the same key already exists, its data is updated.
     *
     * @param key a key of a node to be inserted
     * @param data a value of a node to be inserted
     * @return the inserted node, or the updated node if node with that key already exists.
     */
    override fun insert(key: K, data: V): BSTNode<K, V> {
        val resultNode: BSTNode<K, V> = BSTNode(key, data)
        var parent: BSTNode<K, V>? = null
        var currentNode: BSTNode<K, V>? = root
        while (currentNode != null && currentNode.key != key) {
            parent = currentNode
            currentNode = stepDeep(currentNode, key)
        }
        if (parent != null) {
            if (parent.key > resultNode.key) {
                parent.left = replaceByUpdating(parent.left, resultNode)
            } else if (parent.key < resultNode.key) {
                parent.right = replaceByUpdating(parent.right, resultNode)
            }
        } else {
                root = replaceByUpdating(root, resultNode)
        }
        return resultNode
    }

    /**
     * Deletes the node with the specified [key] from the AVL tree.
     *
     * @param key the key of the node to be deleted.
     * @return the deleted node or null, if impossible to find such node
     */
    override fun delete(key: K): BSTNode<K, V>? {
        var result: BSTNode<K, V>? = root
        var target: BSTNode<K, V>? = root
        var current: BSTNode<K, V>? = null
        while (target != null) {
            // If the target node has been found
            if (target.key == key) {
                result = target
                if (current == null) {
                    // If currentNode == null, then we haven't gone anywhere from the root.
                    root = replaceWithAppending(target, findMin(target.right))
                } else if (current.left == target) {
                    current.left = replaceWithAppending(target, findMin(target.right))
                } else current.right = replaceWithAppending(target, findMin(target.right))
                break
            }
            current = target
            target = if (target.key > key) {
                target.left
            } else {
                target.right
            }
        }
        return result
    }

    private fun replaceWithAppending(target: BSTNode<K, V>, required: BSTNode<K, V>?): BSTNode<K, V>? {
        val result: BSTNode<K, V>? = if (target.right == null) {
            target.left
        } else if (target.left == null) {
            target.right
        } else {
            if (required != null) {
                required.left = target.left
            }
            target.right
        }
        return result
    }

    private fun replaceByUpdating(target: BSTNode<K, V>?, required: BSTNode<K, V>): BSTNode<K, V>{
        if (target?.key == required.key){
            target.data = required.data
            return target
        }
        else{
            return required
        }
    }

    private fun stepDeep(node: BSTNode<K, V>, key: K): BSTNode<K, V>? {
        val result: BSTNode<K, V>? =
        if (node.key > key) {
            node.left
        } else if (node.key < key) {
            node.right
        }
        else {
            node
        }

        return result
    }
}
