package org.treelib

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.RepeatedTest
import kotlin.random.Random
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertNotNull

const val RANDOM_NUMBER_MAX_VALUE = 10000
const val RANDOM_NUMBER_MIN_VALUE = 0
const val RANDOM_NUMBER_COUNT = 100

class RedBlackTreeUnitTests {
    lateinit var intTree: RedBlackTree<Int, Int>

    @BeforeTest
    fun before() {
        intTree = RedBlackTree<Int, Int>()

    }

    @Test
    fun `Tree is empty right after initialization`() {
        try {
            intTree.delete(0)
        } catch (e: NoSuchElementException) {
            assertEquals(e.message, "Tree is empty")
        }
    }

    @Test
    fun `insert 1`() {
        intTree.insert(1,1)
        assertEquals(intTree.iterator().next().second, 1)
    }

    @Test
    fun `insert 2`() {
        val arr = arrayOf(256 ,247, 257)
        intTree.insert(0, arr[0])
        intTree.insert(1, arr[1])
        intTree.insert(2, arr[2])
        for ((i, j) in intTree.iterator()) {
            assertEquals(j, arr[i])
        }
    }

    @Test
    fun `delete 1 `() {
        intTree.insert(23, 2)
        intTree.insert(54, 1)
        intTree.insert(70, 3)
        intTree.insert(21, 3)
        intTree.insert(1, 3)
        assertEquals(intTree.delete(1), 3)
        intTree.checkAll()
    }

    @Test
    fun `Insert overwrites values`() {
        intTree.insert(54, 1)
        intTree.insert(23, 2)
        intTree.insert(70, 3)
        assertEquals(intTree.search(70), 3)
        intTree.insert(70, 10)
        assertEquals(intTree.search(70), 10)
    }

    @Test
    fun `deleteMin deletes minimum element`() {
        intTree.insert(54, 1)
        intTree.insert(23, 2)
        intTree.insert(70, 3)
        intTree.insert(19, 4)
        intTree.insert(50, 5)
        val minValue = intTree.findMin()
        intTree.deleteMin()
        minValue?.let {
            assert(intTree.findMin() != minValue)
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
            assertEquals(e.message, "Tree is empty")
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
        try {
            intTree.delete(1)
        } catch (e: NoSuchElementException) {
            assertEquals(e.message, "No such node to be deleted")
        }

    }

    @Test
    fun `delete tree with two black children`() {
        intTree.insert(54, 1)
        intTree.insert(23, 2)
        intTree.insert(70, 3)
        assertNotNull(intTree.delete(54))
        intTree.checkAll()
    }

    @Test
    fun `deleteMin tree with two black children`() {
        intTree.insert(54, 1)
        intTree.insert(23, 2)
        intTree.insert(70, 3)
        assertNotNull(intTree.deleteMin())
        intTree.checkAll()
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
    }

    @AfterTest
    fun after() {
        randomTree.checkAll()
    }

    @RepeatedTest(10)
    fun `Invariant after initialization`() {
        randomTree.checkAll()
    }

    @RepeatedTest(10)
    fun `Search function test`() {
        for (i in 0..(randomKeys.size - 1)) {
            var value = randomTree.search(randomKeys[i])
            assertEquals(value, i)
        }
    }

    @RepeatedTest(10)
    fun `DeleteMin Test`() {
        randomKeys.sort()
        for (key in randomKeys) {
            randomTree.checkAll()
            randomTree.deleteMin()
            var node = randomTree.search(key)
            assertEquals(node, null)
        }
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
    }
}
