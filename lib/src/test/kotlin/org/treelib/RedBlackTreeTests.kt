package org.treelib

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.RepeatedTest
import org.junit.platform.commons.logging.Logger
import java.lang.annotation.Repeatable
import kotlin.math.abs
import kotlin.random.Random
import kotlin.test.BeforeTest

class RBTreeInvariantCheck<K: Comparable<K>, V: Any>(var tree: RedBlackTree<K, V>) {

    fun isBlackBalanced(): Boolean {
        return subtreeBlackCountDifference(tree.root) != -1
    }

    private fun subtreeBlackCountDifference(node: RBNode<K, V>?): Int {
        node?.let {
            var left = subtreeBlackCountDifference(node.left)
            var right = subtreeBlackCountDifference(node.right)
            if (left == right) {
                return left
            } else {
                return -1
            }

        } ?:let {
            return 1
        }
    }
}

class RedBlackTreeTests {
    lateinit var intTree: RedBlackTree<Int, Int>
    lateinit var check: RBTreeInvariantCheck<Int, Int>
    @BeforeTest
    fun before() {
        intTree = RedBlackTree<Int, Int>()
        check = RBTreeInvariantCheck<Int, Int>(intTree)
    }
    @Test
    fun `assert root is null right after initialization`() {
        assertNull(intTree.root)
    }

    @Test
    fun `insert 1`() {
        intTree.insert(1,1)
        assertEquals(intTree.root?.data, 1)
        assertEquals(intTree.root?.key, 1)
    }

    @Test
    fun `insert 2`() {
        intTree.insert(2, 257)
        intTree.insert(1, 256)
        intTree.insert(3, 247)
        assertEquals(intTree.root?.data, 257)
        assertEquals(intTree.root?.left?.data, 256)
        assertEquals(intTree.root?.right?.data, 247)
    }

    @Test
    fun `is perfect black balanced 1`() {
        intTree.insert(10, 1)
        intTree.insert(44, 2)
        intTree.insert(3, 3)
        intTree.insert(88, 4)
        intTree.insert(2, 5)
        intTree.insert(86, 6)
        intTree.insert(20, 7)
        intTree.insert(60, 8)
        var bool = check.isBlackBalanced()
        assertEquals(bool, true)
    }

    @RepeatedTest(10)
    fun `is perfect black balanced 2`() {
        for (i in 1..25) {
            var randomKey = Random.nextInt(0,1000)
            intTree.insert(randomKey, i)
            println("intTree.insert($randomKey, $i)")
        }

        assertEquals(check.isBlackBalanced(), true)
    }



}
