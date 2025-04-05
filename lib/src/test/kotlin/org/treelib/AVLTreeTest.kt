package org.treelib

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.math.abs

class AVLTreeTest {
	@Test
	@DisplayName("Insert: root")
	fun insert_root() {
		val tree = AVLTree<Int, String>()
		tree.insert(1, "one")
		assertNotNull(tree.root)
		assertEquals(1, tree.root?.key)
	}

	@Test
	@DisplayName("Insert: single node")
	fun insert_singleNode_returnsNewNodeAndSetsRoot() {
		val tree = AVLTree<Int, String>(AVLNode(1, "one"))
		tree.insert(10, "ten")
		val inserted = tree.search(10)
		assertNotNull(inserted)
		assertEquals(10, inserted?.key)
		assertEquals("ten", inserted?.data)
		assertSame(inserted, tree.root?.right)
	}

	@Test
	@DisplayName("Insert: right rotation")
	fun insert_rightRotation() {
		val tree = AVLTree<Int, String>(AVLNode(3, "three"))
		tree.insert(2, "two")
		tree.insert(1, "one")
		assertEquals(2, tree.root?.key)
		assertEquals(1, tree.root?.left?.key)
		assertEquals(3, tree.root?.right?.key)
	}

	@Test
	@DisplayName("Insert: left rotation")
	fun insert_leftRotation() {
		val tree = AVLTree<Int, String>()
		tree.insert(1, "one")
		tree.insert(2, "two")
		tree.insert(3, "three")
		val root = tree.root!!
		assertEquals(2, root.key)
		assertEquals(1, root.left?.key)
		assertEquals(3, root.right?.key)
	}

	@Test
	@DisplayName("Insert: left-right rotation")
	fun insert_leftRightRotation() {
		val tree = AVLTree<Int, String>()
		tree.insert(3, "three")
		tree.insert(1, "one")
		tree.insert(2, "two")
		val root = tree.root!!
		assertEquals(2, root.key)
		assertEquals(1, root.left?.key)
		assertEquals(3, root.right?.key)
	}

	@Test
	@DisplayName("Insert: right-left rotation")
	fun insert_rightLeftRotation() {
		val tree = AVLTree<Int, String>()
		tree.insert(1, "one")
		tree.insert(3, "three")
		tree.insert(2, "two")
		val root = tree.root!!
		assertEquals(2, root.key)
		assertEquals(1, root.left?.key)
		assertEquals(3, root.right?.key)
	}

	@Test
	@DisplayName("Delete: delete root")
	fun delete_rootDelete() {
		val tree = AVLTree<Int, String>(AVLNode(5, "five"))
		tree.delete(5)
		assertNull(tree.root)
	}

	@Test
	@DisplayName("Delete: one child")
	fun delete_oneChild() {
		val tree = AVLTree(AVLNode(1, "one"))
		tree.insert(2, "two")
		tree.insert(3, "three")
		tree.insert(4, "four")
		tree.delete(3)
		assertNull(tree.search(3))
		val node4 = tree.search(4)
		assertNotNull(node4)
		assertEquals(4, node4?.key)
	}

	@Test
	@DisplayName("Delete: two children")
	fun delete_nodeWithTwoChildren_replacesWithPredecessor() {
		val tree = AVLTree<Int, String>(AVLNode(10, "ten"))
		tree.insert(5, "five")
		tree.insert(15, "fifteen")
		tree.insert(7, "seven")
		tree.delete(10)
		assertEquals(7, tree.root?.key)
		assertEquals("seven", tree.root?.data)
	}

	@Test
	@DisplayName("Delete: nonexistent key")
	fun delete_nonexistentKey_returnsNullAndLeavesTreeUnchanged() {
		val tree = AVLTree<Int, String>(AVLNode(1, "one"))
		assertFailsWith<NoSuchElementException> { tree.delete(42) }
		assertEquals(1, tree.root?.key)
	}

	@Test
	@DisplayName("Iterator: empty tree")
	fun iterator_emptyTree() {
		val tree = AVLTree<Int, String>()
		val iter = tree.iterator()
		assertFalse(iter.hasNext())
		assertThrows(NoSuchElementException::class.java) { iter.next() }
	}

	@Test
	@DisplayName("Iterator: single element")
	fun iterator_singleElement() {
		val tree = AVLTree<Int, String>()
		tree.insert(42, "forty‑two")
		val iter = tree.iterator().iterator()
		assertTrue(iter.hasNext())
		assertEquals("forty‑two", iter.next())
		assertFalse(iter.hasNext())
		assertThrows(NoSuchElementException::class.java) { iter.next() }
	}

	@Test
	@DisplayName("Iterator: next() throws after exhaustion")
	fun iterator_nextThrowsAfterExhaustion() {
		val tree = AVLTree<Int, String>()
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
		val tree = AVLTree<Int, String>()
		assertNull(tree.search(42))
	}

	@Test
	@DisplayName("Search: root only")
	fun search_rootOnly() {
		val tree = AVLTree<Int, String>()
		tree.insert(5, "five")
		val found = tree.search(5)
		assertNotNull(found)
		assertEquals(5, found?.key)
		assertEquals("five", found?.data)
	}

	@Test
	@DisplayName("Search: nonexistent key returns null")
	fun search_nonexistentKey() {
		val tree = AVLTree<Int, String>()
		tree.insert(10, "ten")
		tree.insert(20, "twenty")
		assertNull(tree.search(0))
		assertNull(tree.search(15))
		assertNull(tree.search(30))
	}

	@Test
	@DisplayName("Search: multiple nodes returns correct data")
	fun search_multipleNodes() {
		val tree = AVLTree<Int, String>()
		listOf(10 to "ten", 5 to "five", 15 to "fifteen", 7 to "seven").forEach { (k, v) -> tree.insert(k, v) }
		for ((key, value) in listOf(10 to "ten", 5 to "five", 15 to "fifteen", 7 to "seven")) {
			val found = tree.search(key)
			assertNotNull(found)
			assertEquals(value, found?.data)
		}
	}

	@Test
	@DisplayName("Search: after rotations still finds all keys")
	fun search_afterRotations() {
		val tree = AVLTree<Int, String>()
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
			5 to "five"
		).forEach { (k, v) ->
			val found = tree.search(k)
			assertNotNull(found, "Expected to find key $k")
			assertEquals(v, found?.data)
		}
	}

	class AVLTreeParameterizedTests {
		private fun height(node: AVLNode<Int, Int>?): Int {
			return node?.height ?: 0
		}

		private fun isBalanced(node: AVLNode<Int, Int>?): Boolean {
			return if (node == null) true else if (abs(height(node.left) - height(node.right)) > 1) false
			else isBalanced(node.left) && isBalanced(node.right)
		}

		private fun inOrder(node: AVLNode<Int, Int>?): List<Int> {
			return if (node == null) emptyList() else inOrder(node.left) + listOf(node.key) + inOrder(node.right)
		}

		companion object {
			@JvmStatic
			fun listProvider(): Stream<List<Int>> {
				return Stream.of(
					emptyList(),
					listOf(1),
					listOf(3, 2, 1),
					listOf(5, 2, 8, 1, 3),
					(1..20).toList().shuffled(),
					(1..100).toList().shuffled()
				)
			}
		}

		@ParameterizedTest(name = "In-order traversal returns sorted keys for list: {0}")
		@MethodSource("listProvider")
		fun inorderTraversalReturnsSortedKeys(list: List<Int>) {
			val tree = AVLTree<Int, Int>()
			list.forEach { key -> tree.insert(key, key) }
			val inOrderKeys = inOrder(tree.root)
			assertEquals(inOrderKeys.sorted(), inOrderKeys, "In-order traversal should be sorted")
		}

		@ParameterizedTest(name = "Search returns correct values for list: {0}")
		@MethodSource("listProvider")
		fun searchShouldReturnCorrectValuesForInsertedKeys(list: List<Int>) {
			val tree = AVLTree<Int, Int>()
			list.forEach { key -> tree.insert(key, key * 2) }
			list.forEach { key ->
				val node = tree.search(key)
				assertNotNull(node, "Node with key $key should be found")
				assertEquals(key * 2, node!!.data, "For key $key, expected value ${key * 2}")
			}
		}

		@ParameterizedTest(name = "Tree remains balanced after insertions and deletions for list: {0}")
		@MethodSource("listProvider")
		fun treeShouldRemainBalancedAfterInsertionsAndDeletions(list: List<Int>) {
			val tree = AVLTree<Int, Int>()
			list.forEach { key -> tree.insert(key, key) }
			assertTrue(isBalanced(tree.root), "Tree should be balanced after insertions")
			val uniqueSorted = list.toSet().sorted()
			uniqueSorted.forEachIndexed { index, key ->
				if (index % 2 == 0) {
					tree.delete(key)
					assertTrue(isBalanced(tree.root), "Tree should remain balanced after deleting key $key")
				}
			}
		}
	}
}
