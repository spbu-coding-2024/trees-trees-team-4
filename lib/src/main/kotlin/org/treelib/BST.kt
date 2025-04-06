package org.treelib

/**
 * Represents a node in the binary search tree.
 *
 * @param K the type of keys maintained by this node.
 * @param D the type of mapped values.
 * @property key the key associated with this node.
 * @property data the value stored in this node.
 */
class BSTNode<K : Comparable<K>, D : Any>(key: K, data: D) : Node<K, D, BSTNode<K, D>>(key, data)

/**
 * Binary search tree that stores key-value pairs in a sorted order, allowing efficient search, insertion, and deletion.
 *
 * @param K the type of keys maintained by this tree. Keys must be comparable.
 * @param D the type of mapped values.
 * @property root the root node of the binary search tree, or null if the tree is empty.
 */
class BST<K : Comparable<K>, D : Any>(override var root: BSTNode<K, D>? = null) :
    BinaryTree<K, D, BSTNode<K, D>>(root) {

    /**
     * Inserts a new node with the specified [key] and [data] into the binary search tree.
     *
     * If a node with the same key already exists, its data is updated.
     *
     * @param key a key of a node to be inserted
     * @param data a value of a node to be inserted
     * @return the inserted node, or the updated node if node with that key already exists.
     */
    override fun insert(key: K, data: D) {
        val newNode: BSTNode<K, D> = BSTNode(key, data)
        var parent: BSTNode<K, D>? = null
        var currentNode: BSTNode<K, D>? = root
        while (currentNode != null && currentNode.key != key) {
            parent = currentNode
            currentNode = stepDeep(currentNode, key)
        }
        if (parent != null) {
            if (parent.key > newNode.key) {
                parent.left = replaceByUpdating(parent.left, newNode)
            } else if (parent.key < newNode.key) {
                parent.right = replaceByUpdating(parent.right, newNode)
            }
        } else {
                root = replaceByUpdating(root, newNode)
        }
    }

    /**
     * Deletes the node with the specified [key] from the AVL tree.
     *
     * @param key the key of the node to be deleted.
     * @return the deleted node or null, if impossible to find such node
     */
    override fun delete(key: K): D? {
        var result: D? = null
        var target: BSTNode<K, D>? = root
        var current: BSTNode<K, D>? = null
        while (target != null) {
            // If the target node has been found
            if (target.key == key) {
                result = target.data
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

    private fun replaceWithAppending(target: BSTNode<K, D>, required: BSTNode<K, D>?): BSTNode<K, D>? {
        val result: BSTNode<K, D>? = if (target.right == null) {
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

    private fun replaceByUpdating(target: BSTNode<K, D>?, required: BSTNode<K, D>): BSTNode<K, D>{
        if (target?.key == required.key){
            target.data = required.data
            return target
        }
        else{
            return required
        }
    }

    private fun stepDeep(node: BSTNode<K, D>, key: K): BSTNode<K, D>? {
        val result: BSTNode<K, D>? =
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
