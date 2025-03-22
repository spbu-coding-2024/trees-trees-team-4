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
    fun assertRedLinkAreLeaningLeft() {
        assertRedLinkAreLeaningLeft(tree.root)
    }
    private fun assertRedLinkAreLeaningLeft(node: RBNode<K, V>?) {
        node?.let {
            if (node.rightIsRed()) {
                throw Exception("Tree with right leaning red link")
            }
            assertRedLinkAreLeaningLeft(node.left)
            assertRedLinkAreLeaningLeft(node.right)
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
        var arr = arrayOf(10, 44, 3, 88, 2, 86, 20, 60)
        arr.forEachIndexed { ind, value -> intTree.insert(value , ind)}
//        intTree.insert(10, 1)
//        intTree.insert(44, 2)
//        intTree.insert(3, 3)
//        intTree.insert(88, 4)
//        intTree.insert(2, 5)
//        intTree.insert(86, 6)
//        intTree.insert(20, 7)
//        intTree.insert(60, 8)
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

    @RepeatedTest(10)
    fun `tree is with red leaning links`() {
        for (i in 1..25) {
            var randomKey = Random.nextInt(0,1000)
            intTree.insert(randomKey, i)
            println("intTree.insert($randomKey, $i)")
        }
        check.assertRedLinkAreLeaningLeft()
    }

    @Test
    fun `search function test 1`() {
        var arr = arrayOf(10, 44, 3, 88, 2, 86, 20, 60)
        arr.forEachIndexed { ind, value -> intTree.insert(value , ind)}
        for (i in 0..(arr.size - 1)) {
            var value = intTree.search(arr[i])?.data
            assertEquals(value, i)
        }
    }

    @RepeatedTest(10)
    fun `search function test 2`() {
        var arr = Array<Int>(25) { 0 }
        for (i in 0..24) {
            arr[i] = Random.nextInt(0,1000)
            intTree.insert(arr[i], i)
            println("intTree.insert(${arr[i]}, $i)")
        }
        for (i in 0..24) {
            var value = intTree.search(arr[i])?.data
            assertEquals(value, i)
        }
    }

    @Test
    fun `deleteMin 1`() {
        var arr = arrayOf(10, 44, 3, 88, 2, 86, 20, 60)
        arr.forEachIndexed { ind, value -> intTree.insert(value , ind)}
        for (i in 1..8) {
            intTree.deleteMin()
            kotlin.test.assertEquals(intTree.search(arr[i]), null)
        }
        assertEquals(intTree.root, null)
    }
}
