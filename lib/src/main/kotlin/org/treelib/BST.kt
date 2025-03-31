package org.treelib

class BSTNode<K : Comparable<K>, V : Any>(key: K, data: V) : Node<K, V, BSTNode<K, V>>(key, data)

class BST<K : Comparable<K>, V : Any>(override var root: BSTNode<K, V>? = null) :
    BinaryTree<K, V, BSTNode<K, V>>(root) {

    override fun insert(key: K, data: V): BSTNode<K, V> {
        val resultNode: BSTNode<K, V> = BSTNode(key, data)
        var currentNode: BSTNode<K, V>? = root
        while (currentNode?.key != null) {
            currentNode = if (currentNode.key > resultNode.key) {
                currentNode.left
            } else {
                currentNode.right
            } ?: break
        }
        if (currentNode != null) {
            if (currentNode.key > resultNode.key) {
                currentNode.left = resultNode
            } else {
                currentNode.right = resultNode
            }
        } else {
            root = resultNode
        }
        return resultNode
    }

    override fun delete(key: K): BSTNode<K, V>? {
        var result: BSTNode<K, V>? = root
        var target: BSTNode<K, V>? = root
        var current: BSTNode<K, V>? = null
        while (target != null) {
            // Если нашли нужную ноду
            if (target.key == key) {
                result = target
                if (current == null) {
                    // Если currentNode == null, то из корня мы никуда не ушли
                    root = replaceWithAppending(target, findMin(target.right))
                } else if (current.left == target) {
                    current.left = replaceWithAppending(target, findMin(target.right))
                }
                else
                    current.right = replaceWithAppending(target, findMin(target.right))
                break;
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
}
