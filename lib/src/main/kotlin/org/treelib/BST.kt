package org.treelib

class BSTNode<K: Comparable<K>, V: Any>(key: K, data: V) : Node<K, V, BSTNode<K, V>>(key, data)

class BST<K: Comparable<K>, V: Any>(override var root: BSTNode<K, V>? = null) :
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
        }
        else{
            root = resultNode
        }
        return resultNode
    }

    override fun delete(key: K): BSTNode<K, V>? {
        var resultNode: BSTNode<K, V>? = root
        var currentNode: BSTNode<K, V>? = null
        var currentRight: BSTNode<K, V>?
        while (resultNode != null) {
            // Если нашли нужную ноду
            if (resultNode.key == key) {
                // Проверяем, каким из потомков является результирующая. Если null - дерево пустое
                if (currentNode?.left == resultNode) {
                    // Подменяем результирующую на ее потомка
                    currentNode.left =
                        if (resultNode.right == null) {
                            resultNode.left
                        } else if (resultNode.left == null) {
                            resultNode.right
                        } else {
                            // Если у результирующей есть оба потомка, фиксируем правого
                            currentRight = resultNode.right
                            // и ищем, куда среди его левых потомков можно вставить левого
                            while (currentRight?.left != null) {
                                currentRight = currentRight.left
                            }
                            if (currentRight != null) {
                                currentRight.left = resultNode.left
                            } else {
                                // Заглушка
                                resultNode.right = null
                            }
                            resultNode.right
                        }
                } else if (currentNode?.right == resultNode) {
                    // Подменяем результирующую на ее потомка
                    currentNode.right =
                        if (resultNode.right == null) {
                            resultNode.left
                        } else if (resultNode.left == null) {
                            resultNode.right
                        } else {
                            // Если у результирующей есть оба потомка, фиксируем правого
                            currentRight = resultNode.right
                            // и ищем, куда среди его левых потомков можно вставить левого
                            while (currentRight?.left != null) {
                                currentRight = currentRight.left
                            }
                            if (currentRight != null) {
                                currentRight.left = resultNode.left
                            } else {
                                // Заглушка
                                resultNode.right = null
                            }
                            resultNode.right
                        }
                } else {
                    // Если искомый узел - корень, удаляем корень
                    root = null
                }
            }
                if (resultNode.key > key) {
                    currentNode = resultNode
                    resultNode = resultNode.left ?: break
                } else if (resultNode.key < key) {
                    currentNode = resultNode
                    resultNode = resultNode.right ?: break
                }
            }
        return resultNode
    }
}