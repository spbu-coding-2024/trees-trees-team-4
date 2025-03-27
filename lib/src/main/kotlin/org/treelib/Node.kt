
package org.treelib


abstract class Node<K: Comparable<K>, V: Any, N: Node<K, V, N>>(var key: K, var data: V) {
    internal var left: N? = null
    internal var right: N? = null
}
