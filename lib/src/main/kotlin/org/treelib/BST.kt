package org.treelib

class BSTNode<K: Comparable<K>, V: Any>(key: K, data: V) : Node<K, V>(key, data)

class BST<K: Comparable<K>, V: Any>(key: K, data: V) : BinaryTree<K, V>(key, data) {

    override var root: Node<K, V>? = BSTNode(key, data)

    override fun insert(key: K, data: V): Node<K, V> {
        val resultNode: BSTNode<K, V> = BSTNode(key, data)
        var currentNode: BSTNode<K, V>? = root as BSTNode<K, V>?
        while (currentNode?.key != null) {
            currentNode =
                if (currentNode.key > resultNode.key) {
                    (currentNode.left ?: break) as BSTNode<K, V>?
                } else {
                    (currentNode.right ?: break) as BSTNode<K, V>?
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

    override fun search(key: K): Node<K, V>? {
        var resultNode: BSTNode<K, V>? = null
        var currentNode: BSTNode<K, V>? = root as BSTNode<K, V>?
        while (currentNode?.key != null) {
            if (currentNode.key > key) {
                currentNode = (currentNode.left ?: break) as BSTNode<K, V>?
            } else if (currentNode.key < key) {
                currentNode = (currentNode.right ?: break) as BSTNode<K, V>?
            } else {
                resultNode = currentNode
            }
        }
        return resultNode
    }

    override fun delete(key: K): Node<K, V>? {
        var resultNode: BSTNode<K, V>? = root as BSTNode<K, V>?
        var currentNode: BSTNode<K, V>? = null
        var currentRight: BSTNode<K, V>
        while (true) {
            if (resultNode?.key == key){
                if (currentNode?.left == resultNode){
                    currentNode.left =
                        if (resultNode.right == null){
                            resultNode.left
                        } else if (resultNode.left == null) {
                            resultNode.right
                        } else {
                            currentRight = resultNode.right as BSTNode<K, V>
                            while (currentRight.left != null){
                                currentRight = currentRight.left as BSTNode<K, V>
                            }
                            currentRight.left = resultNode.left as BSTNode<K, V>
                            resultNode.right
                        }

                }
            }
            if (resultNode?.left == null && resultNode?.right == null){
                break
            }
            if (resultNode.key > key) {
                currentNode = resultNode
                resultNode = resultNode.left as BSTNode<K, V>?
            } else if (resultNode.key > key) {
                currentNode = resultNode
                resultNode = resultNode.right as BSTNode<K, V>?
            }
        }
        return resultNode
    }

    override fun iterator(key: K): Iterable<Node<K, V>> {
        TODO("Not yet implemented")
    }
}