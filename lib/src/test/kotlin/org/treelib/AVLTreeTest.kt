package org.treelib

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertFailsWith

class AVLTreeTest {

    private fun <K : Comparable<K>, D> inOrderKeys(tree: AVLTree<K, D>): List<K> {
        return tree.iterator().asSequence().map { it.first }.toList()
    }

    @Test
    @DisplayName("Insert: root")
    fun insert_root() {
        val tree = AVLTree<Int, String?>()
        tree.insert(1, "one")
        val found = tree.search(1)?.second
        assertNotNull(found)
        assertEquals("one", found)
    }

    @Test
    @DisplayName("Insert: single node")
    fun insert_singleNode_returnsNewNodeAndSetsRoot() {
        val tree = AVLTree<Int, String?>(1, "one")
        tree.insert(10, "ten")
        val inserted = tree.search(10)?.second
        assertNotNull(inserted)
        assertEquals("ten", inserted)
        val keys = inOrderKeys(tree)
        assertEquals(listOf(1, 10), keys)
    }

    @Test
    @DisplayName("Insert: right rotation")
    fun insert_rightRotation() {
        val tree = AVLTree<Int, String?>(3, "three")
        tree.insert(2, "two")
        tree.insert(1, "one")
        assertEquals("one", tree.search(1)?.second)
        assertEquals("two", tree.search(2)?.second)
        assertEquals("three", tree.search(3)?.second)
        val keys = inOrderKeys(tree)
        assertEquals(listOf(1, 2, 3), keys)
    }

    @Test
    @DisplayName("Insert: left rotation")
    fun insert_leftRotation() {
        val tree = AVLTree<Int, String?>()
        tree.insert(1, "one")
        tree.insert(2, "two")
        tree.insert(3, "three")
        assertEquals("one", tree.search(1)?.second)
        assertEquals("two", tree.search(2)?.second)
        assertEquals("three", tree.search(3)?.second)
        val keys = inOrderKeys(tree)
        assertEquals(listOf(1, 2, 3), keys)
    }

    @Test
    @DisplayName("Insert: left-right rotation")
    fun insert_leftRightRotation() {
        val tree = AVLTree<Int, String?>()
        tree.insert(3, "three")
        tree.insert(1, "one")
        tree.insert(2, "two")
        assertEquals("one", tree.search(1)?.second)
        assertEquals("two", tree.search(2)?.second)
        assertEquals("three", tree.search(3)?.second)
        val keys = inOrderKeys(tree)
        assertEquals(listOf(1, 2, 3), keys)
    }

    @Test
    @DisplayName("Insert: right-left rotation")
    fun insert_rightLeftRotation() {
        val tree = AVLTree<Int, String?>()
        tree.insert(1, "one")
        tree.insert(3, "three")
        tree.insert(2, "two")
        assertEquals("one", tree.search(1)?.second)
        assertEquals("two", tree.search(2)?.second)
        assertEquals("three", tree.search(3)?.second)
        val keys = inOrderKeys(tree)
        assertEquals(listOf(1, 2, 3), keys)
    }

    @Test
    @DisplayName("Delete: delete root")
    fun delete_rootDelete() {
        val tree = AVLTree<Int, String?>(5, "five")
        tree.delete(5)
        assertNull(tree.search(5))
        assertEquals(emptyList<Int>(), inOrderKeys(tree))
    }

    @Test
    @DisplayName("Delete: one child")
    fun delete_oneChild() {
        val tree = AVLTree<Int, String?>(1, "one")
        tree.insert(2, "two")
        tree.insert(3, "three")
        tree.insert(4, "four")
        tree.delete(3)
        assertNull(tree.search(3))
        val node4 = tree.search(4)?.second
        assertNotNull(node4)
        assertEquals("four", node4)
        val keys = inOrderKeys(tree)
        assertEquals(listOf(1, 2, 4), keys)
    }

    @Test
    @DisplayName("Delete: two children")
    fun delete_nodeWithTwoChildren_replacesWithPredecessor() {
        val tree = AVLTree<Int, String?>(10, "ten")
        tree.insert(5, "five")
        tree.insert(15, "fifteen")
        tree.insert(7, "seven")
        tree.delete(10)
        assertNull(tree.search(10))
        assertEquals("five", tree.search(5)?.second)
        assertEquals("fifteen", tree.search(15)?.second)
        assertEquals("seven", tree.search(7)?.second)
        val keys = inOrderKeys(tree)
        assertEquals(listOf(5, 7, 15), keys)
    }

    @Test
    @DisplayName("Delete: nonexistent key")
    fun delete_nonexistentKey_returnsNullAndLeavesTreeUnchanged() {
        val tree = AVLTree<Int, String?>(1, "one")
        assertFailsWith<NoSuchElementException> { tree.delete(42) }
        assertEquals("one", tree.search(1)?.second)
    }

    @Test
    @DisplayName("Delete: empty tree")
    fun delete_in_empty_tree_returnsNullAndLeavesTreeUnchanged() {
        val tree = AVLTree<Int, String?>()
        assertFailsWith<NoSuchElementException> { tree.delete(42) }
    }

    @Test
    @DisplayName("Iterator: empty tree")
    fun iterator_emptyTree() {
        val tree = AVLTree<Int, String?>()
        val iter = tree.iterator()
        assertFalse(iter.hasNext())
        assertThrows(NoSuchElementException::class.java) { iter.next() }
    }

    @Test
    @DisplayName("Iterator: single element")
    fun iterator_singleElement() {
        val tree = AVLTree<Int, String?>()
        tree.insert(42, "forty‑two")
        val iter = tree.iterator().iterator()
        assertTrue(iter.hasNext())
        assertEquals("forty‑two", iter.next().second)
        assertFalse(iter.hasNext())
        assertThrows(NoSuchElementException::class.java) { iter.next() }
    }

    @Test
    @DisplayName("Iterator: next() throws after exhaustion")
    fun iterator_nextThrowsAfterExhaustion() {
        val tree = AVLTree<Int, String?>()
        tree.insert(1, "one")
        val iter = tree.iterator().iterator()
        assertTrue(iter.hasNext())
        iter.next()
        assertFalse(iter.hasNext())
        assertThrows(NoSuchElementException::class.java) { iter.next() }
    }

    @Test
    @DisplayName("Search: empty tree returns null")
    fun search_emptyTree() {
        val tree = AVLTree<Int, String?>()
        assertNull(tree.search(42))
    }

    @Test
    @DisplayName("Search: root only")
    fun search_rootOnly() {
        val tree = AVLTree<Int, String?>()
        tree.insert(5, "five")
        val found = tree.search(5)
        assertNotNull(found)
        assertEquals("five", found?.second)
    }

    @Test
    @DisplayName("Search: nonexistent key returns null")
    fun search_nonexistentKey() {
        val tree = AVLTree<Int, String?>()
        tree.insert(10, "ten")
        tree.insert(20, "twenty")
        assertNull(tree.search(0))
        assertNull(tree.search(15))
        assertNull(tree.search(30))
    }

    @Test
    @DisplayName("Search: multiple nodes returns correct data")
    fun search_multipleNodes() {
        val tree = AVLTree<Int, String?>()
        listOf(10 to "ten", 5 to "five", 15 to "fifteen", 7 to "seven").forEach { (k, v) ->
            tree.insert(k, v)
        }
        listOf(10 to "ten", 5 to "five", 15 to "fifteen", 7 to "seven").forEach { (key, value) ->
            val found = tree.search(key)
            assertNotNull(found)
            assertEquals(value, found?.second)
        }
    }

    @Test
    @DisplayName("Search: after rotations still finds all keys")
    fun search_afterRotations() {
        val tree = AVLTree<Int, String?>()
        tree.insert(1, "one")
        tree.insert(2, "two")
        tree.insert(3, "three")
        tree.insert(5, "five")
        tree.insert(4, "four")
        tree.insert(0, "zero")
        tree.insert(-1, "minus one")
        listOf(
            -1 to "minus one",
            0 to "zero",
            1 to "one",
            2 to "two",
            3 to "three",
            4 to "four",
            5 to "five",
        ).forEach { (key, value) ->
            val found = tree.search(key)
            assertNotNull(found, "Expected to find key $key")
            assertEquals(value, found?.second)
        }
    }

    class AVLTreeParameterizedTests {
        companion object {
            @JvmStatic
            fun listProvider(): Stream<List<Int>> {
                return Stream.of(
                    emptyList(),
                    listOf(1),
                    listOf(3, 2, 1),
                    listOf(5, 2, 8, 1, 3),
                    (1..20).toList().shuffled(),
                    (1..100).toList().shuffled(),
                )
            }
        }

        @ParameterizedTest(name = "In-order traversal returns sorted keys for list: {0}")
        @MethodSource("listProvider")
        fun inorderTraversalReturnsSortedKeys(list: List<Int>) {
            val tree = AVLTree<Int, Int?>()
            list.forEach { key -> tree.insert(key, key) }
            val inOrderKeys = tree.iterator().asSequence().map { it.first }.toList()
            assertEquals(inOrderKeys.sorted(), inOrderKeys, "In-order traversal should be sorted")
        }

        @ParameterizedTest(name = "Search returns correct values for list: {0}")
        @MethodSource("listProvider")
        fun searchShouldReturnCorrectValuesForInsertedKeys(list: List<Int>) {
            val tree = AVLTree<Int, Int?>()
            list.forEach { key -> tree.insert(key, key * 2) }
            list.forEach { key ->
                val result = tree.search(key)?.second
                assertNotNull(result, "Node with key $key should be found")
                assertEquals(key * 2, result, "For key $key, expected value ${key * 2}")
            }
        }

        @ParameterizedTest(name = "Tree remains balanced after insertions and deletions for list: {0}")
        @MethodSource("listProvider")
        fun treeShouldRemainBalancedAfterInsertionsAndDeletions(list: List<Int>) {
            val tree = AVLTree<Int, Int?>()
            list.forEach { key -> tree.insert(key, key) }
            val inOrderKeys1 = tree.iterator().asSequence().map { it.first }.toList()
            assertEquals(inOrderKeys1.sorted(), inOrderKeys1, "Tree should be balanced after insertions")
            val uniqueSorted = list.toSet().sorted()
            uniqueSorted.forEachIndexed { index, key ->
                if (index % 2 == 0) {
                    tree.delete(key)
                    val inOrderKeysAfter = tree.iterator().asSequence().map { it.first }.toList()
                    assertEquals(
                        inOrderKeysAfter.sorted(),
                        inOrderKeysAfter,
                        "Tree should remain balanced after deleting key $key",
                    )
                }
            }
        }
    }
}
