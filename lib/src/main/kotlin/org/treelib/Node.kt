
package org.treelib


abstract class Node<K: Comparable<K>, D: Any?, N: Node<K, D?, N>>(var key: K, var data: D?) {
    internal var left: N? = null
    internal var right: N? = null
}
