package org.treelib

class BSTNode<K : Comparable<K>, V : Any>(key: K, data: V) : Node<K, V, BSTNode<K, V>>(key, data)

class BST<K : Comparable<K>, V : Any>(override var root: BSTNode<K, V>? = null) :
    BinaryTree<K, V, BSTNode<K, V>>(root) {

    override fun insert(key: K, data: V): BSTNode<K, V> {
        val resultNode: BSTNode<K, V> = BSTNode(key, data)
        var currentNode: BSTNode<K, V>? = root
        while (currentNode?.key != null) {
            currentNode =
                if (currentNode.key > resultNode.key) {
                    (currentNode.left ?: break)
                } else {
                    (currentNode.right ?: break)
                }
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
        var targetNode: BSTNode<K, V>? = root
        var currentNode: BSTNode<K, V>? = null
        val minimum: BSTNode<K, V>?
        while (targetNode != null) {
            // Если нашли нужную ноду
            if (targetNode.key == key) {
                result = targetNode
                if (currentNode == null) {
                    // Если currentNode == null, то из корня мы никуда не ушли, проверяем корень
                    root =
                        if (targetNode.right == null) {
                            targetNode.left
                        } else if (targetNode.left == null) {
                            targetNode.right
                        } else {
                            minimum = findMin(targetNode.right)
                            if (minimum != null) {
                                minimum.left = targetNode.left
                            }
                            targetNode.right
                        }
                } else {
                    // Проверяем, каким из потомков является результирующая.
                    if (currentNode.left == targetNode) {
                        // Подменяем результирующую на ее потомка
                        currentNode.left =
                            if (targetNode.right == null) {
                                targetNode.left
                            } else if (targetNode.left == null) {
                                targetNode.right
                            } else {
                                minimum = findMin(targetNode.right)
                                if (minimum != null) {
                                    minimum.left = targetNode.left
                                }
                                targetNode.right
                            }
                    } else {
                        // Подменяем результирующую на ее потомка
                        currentNode.right =
                            if (targetNode.right == null) {
                                targetNode.left
                            } else if (targetNode.left == null) {
                                targetNode.right
                            } else {
                                minimum = findMin(targetNode.right)
                                if (minimum != null) {
                                    minimum.left = targetNode.left
                                }
                                targetNode.right
                            }
                    }
                }
                break;
            }
            if (targetNode.key > key) {
                currentNode = targetNode
                targetNode = targetNode.left ?: break
            } else if (targetNode.key < key) {
                currentNode = targetNode
                targetNode = targetNode.right ?: break
            }
        }
        return result
    }
}