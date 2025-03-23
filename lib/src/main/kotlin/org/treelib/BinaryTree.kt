
package org.treelib


abstract class BinaryTree<K: Comparable<K>, V: Any, N> {
    internal var root: N? = null
    abstract fun min(): N?
    abstract fun max(): N?
    abstract fun insert(key: K, value: V): N
    abstract fun search(key: K): N?
    abstract fun delete(key: K): N?
    abstract fun iterator(key: K): Iterable<N>

}