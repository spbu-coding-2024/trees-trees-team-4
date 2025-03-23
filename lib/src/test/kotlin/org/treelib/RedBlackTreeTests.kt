package org.treelib

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.RepeatedTest
import kotlin.random.Random
import kotlin.test.BeforeTest

const val RANDOM_NUMBER_MAX_VALUE = 100
const val RANDOM_NUMBER_MIN_VALUE = 0
const val RANDOM_NUMBER_COUNT = 8

class RBTreeInvariantCheck<K: Comparable<K>, V: Any>(var tree: RedBlackTree<K, V>) {

    fun isBlackBalanced() {
        if (subtreeBlackCountDifference(tree.root) == -1) {
            error("Invariant failed: tree is not perfect black balanced")
        }
    }

    private fun subtreeBlackCountDifference(node: RBNode<K, V>?): Int {
        node?.let {
            var left = subtreeBlackCountDifference(node.left)
            var right = subtreeBlackCountDifference(node.right)
            return if (left == right) {
                left
            } else {
                -1
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
            if (tree.isRed(node.right)) {
                error("Invariant failed: tree is with right leaning red link")
            }
            assertRedLinkAreLeaningLeft(node.left)
            assertRedLinkAreLeaningLeft(node.right)
        }
    }
    private fun countNodes(node: RBNode<K, V>?): Int {
        if (node == null) return 0
        return countNodes(node.left) + 1 + countNodes(node.right)
    }
    fun sizeIsCorrect() {
        if (countNodes(tree.root) != tree.size) {
            error("Invariant failed: size is not correct")
        }
    }

    fun checkAll() {
        sizeIsCorrect()
        assertRedLinkAreLeaningLeft()
        isBlackBalanced()
    }
}

class RedBlackTreeUnitTests {
    lateinit var intTree: RedBlackTree<Int, Int>
    lateinit var check: RBTreeInvariantCheck<Int, Int>
    @BeforeTest
    fun before() {
        intTree = RedBlackTree<Int, Int>()
        check = RBTreeInvariantCheck<Int, Int>(intTree)
    }
    @Test
    fun `Assert root is null right after initialization`() {
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
}

class RedBlackTreePropertyBasedTests {
    lateinit var randomTree: RedBlackTree<Int, Int>
    lateinit var check: RBTreeInvariantCheck<Int, Int>
    lateinit var randomKeys: Array<Int>

    fun printRandomKeys() {
        randomKeys.forEach { print("$it ") }
        println()
    }

    @BeforeTest
    fun before() {
        randomTree = RedBlackTree<Int, Int>()
        randomKeys = Array<Int>(RANDOM_NUMBER_COUNT) {
            Random.nextInt(RANDOM_NUMBER_MIN_VALUE, RANDOM_NUMBER_MAX_VALUE)
        }
        randomKeys = randomKeys.distinct().toTypedArray()
        randomKeys.forEachIndexed { ind, value -> randomTree.insert(value, ind) }
        check = RBTreeInvariantCheck<Int, Int>(randomTree)
    }

    @RepeatedTest(10)
    fun `Is perfect black balanced`() {
        check.isBlackBalanced()
    }

    @RepeatedTest(10)
    fun `Tree is with red leaning links`() {
        check.assertRedLinkAreLeaningLeft()
    }

    @RepeatedTest(10)
    fun `Search function test`() {
        for (i in 0..(randomTree.size - 1)) { //TODO("iterate")
            var value = randomTree.search(randomKeys[i])?.data
            assertEquals(value, i)
        }
    }

    @RepeatedTest(10)
    fun `DeleteMin Test`() {
        randomKeys.sort()
        for (key in randomKeys) {
            check.checkAll()
            randomTree.deleteMin()
            var node = randomTree.search(key)
            assertEquals(node, null)
        }
        assertEquals(randomTree.root, null)
    }

    @RepeatedTest(20)
    fun `DeleteMax Test`() {
        printRandomKeys()
        randomKeys.sort()

        for (i in (randomKeys.size - 1) downTo 0) {
            randomTree.deleteMax()
            var node = randomTree.search(randomKeys[i])
            assertEquals(node, null)
            check.checkAll()
        }
        assertEquals(randomTree.root, null)
    }
    @RepeatedTest(50)
    fun `Delete Test`() {
        var indexes = Array<Int>(randomKeys.size) { it }
        indexes.shuffle()
        for (i in indexes) {
            randomTree.delete(randomKeys[i])
            var node = randomTree.search(randomKeys[i])
            assertEquals(node, null)
        }
        assertEquals(randomTree.root, null)
    }
}
