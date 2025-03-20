
package org.treelib


abstract class Node<K: Comparable<K>, V: Any>(var key: K, var data: V) {
    internal var left: Node<K, V>? = null
    internal var right: Node<K, V>? = null
}