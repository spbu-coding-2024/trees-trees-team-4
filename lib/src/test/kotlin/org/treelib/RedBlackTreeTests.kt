package org.treelib

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.RepeatedTest
import kotlin.random.Random
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertIs
import kotlin.test.assertNotNull

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

    private fun printTree(node: RBNode<K, V>?, indent: Int) {
        node?.let {
            node.right?.let { printTree(node.right, indent + 4) }
            node.left?.let { printTree(node.left, indent + 4) }
            if (indent > 0) {
                print(" ".repeat(indent + 1))
            }
            println(if (node.color == RED) "${node.key} R" else "${node.key} B")
        }
    }

    fun printTree() {
        tree.root?.let{
            printTree(tree.root, 0)
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

    @Test
    fun `delete 1 `() {
        intTree.insert(23, 2)
        intTree.insert(54, 1)
        intTree.insert(70, 3)
        intTree.insert(21, 3)
        intTree.insert(1, 3)
        check.printTree()
        assertEquals(intTree.delete(1)?.data, 3)
        check.checkAll()
    }

    @Test
    fun `Empty tree is indeed empty`() {
        assertEquals(intTree.isEmpty(), true)
    }

    @Test
    fun `Size is consistent 1`() {
        intTree.insert(54, 1)
        intTree.insert(23, 2)
        intTree.insert(70, 3)
        intTree.insert(19, 4)
        intTree.insert(50, 5)
        check.printTree()
        intTree.delete(23)
        check.printTree()
        check.sizeIsCorrect()
    }

    @Test
    fun `Insert overwrites values`() {
        intTree.insert(54, 1)
        intTree.insert(23, 2)
        intTree.insert(70, 3)
        check.printTree()
        assertEquals(intTree.search(70)?.data, 3)
        intTree.insert(70, 10)
        assertEquals(intTree.search(70)?.data, 10)
    }

    @Test
    fun `deleteMin deletes minimum element`() {
        intTree.insert(54, 1)
        intTree.insert(23, 2)
        intTree.insert(70, 3)
        intTree.insert(19, 4)
        intTree.insert(50, 5)
        check.printTree()
        val minValue = intTree.findMin()
        intTree.deleteMin()
        minValue?.let {
            assertEquals(intTree.search(it.key), null)
        } ?: assert(false)
    }

    @Test
    fun `search returns null for empty tree`() {
        assertEquals(intTree.search(1), null)
    }

    @Test
    fun `delete throws error for empty tree`() {
        try {
            intTree.delete(1)
        } catch (e: NoSuchElementException) {
            assertEquals(e.message, "Nothing to delete")
        }
    }

    @Test
    fun `deleteMin throws error for empty tree`() {
        try {
            intTree.deleteMin()
        } catch (e: NoSuchElementException) {
            assertEquals(e.message, "Nothing to delete")
        }
    }

    @Test
    fun `delete return null if there are no such element`() {
        intTree.insert(54, 1)
        intTree.insert(23, 2)
        intTree.insert(70, 3)
        intTree.insert(19, 4)
        intTree.insert(50, 5)
        val deletedNode = intTree.delete(1)
        assertEquals(deletedNode, null)
    }

    @Test
    fun `delete tree with two black children`() {
        intTree.insert(54, 1)
        intTree.insert(23, 2)
        intTree.insert(70, 3)
        assertNotNull(intTree.delete(54))
        check.checkAll()
    }

    @Test
    fun `deleteMin tree with two black children`() {
        intTree.insert(54, 1)
        intTree.insert(23, 2)
        intTree.insert(70, 3)
        assertNotNull(intTree.deleteMin())
        check.checkAll()
    }

    @Test
    fun `search function return null if there are no element`() {
        intTree.insert(54, 1)
        intTree.insert(23, 2)
        intTree.insert(70, 3)
        assertNull(intTree.search(1234))
    }

}

class RedBlackTreePropertyBasedTests {
    lateinit var randomTree: RedBlackTree<Int, Int>
    lateinit var check: RBTreeInvariantCheck<Int, Int>
    lateinit var randomKeys: Array<Int>

    @BeforeTest
    fun before() {
        randomTree = RedBlackTree<Int, Int>()
        randomKeys = Array<Int>(RANDOM_NUMBER_COUNT) {
            Random.nextInt(RANDOM_NUMBER_MIN_VALUE, RANDOM_NUMBER_MAX_VALUE)
        }
        randomKeys = randomKeys.distinct().toTypedArray()
        randomKeys.forEachIndexed { ind, value -> randomTree.insert(value, ind) }
        randomKeys.forEach { print("$it ") }
        println()
        check = RBTreeInvariantCheck<Int, Int>(randomTree)
    }

    @AfterTest
    fun after() {
        check.checkAll()
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
