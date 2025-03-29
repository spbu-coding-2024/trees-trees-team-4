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
		val inserted = tree.insert(10, "ten")

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
		tree.insert(1, "one")  // triggers right rotation

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
		tree.insert(3, "three")  // triggers single left rotation

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
		tree.insert(2, "two")  // triggers left-right rotation

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
		tree.insert(2, "two")  // triggers right-left rotation

		val root = tree.root!!
		assertEquals(2, root.key)
		assertEquals(1, root.left?.key)
		assertEquals(3, root.right?.key)
	}

	@Test
	@DisplayName("Delete: delete root")
	fun delete_rootDelete() {
		val tree = AVLTree<Int, String>(AVLNode(5, "five"))
		val deleted = tree.delete(5)

		assertNull(deleted)
		assertNull(tree.root)
	}

	@Test
	@DisplayName("Delete: one child")
	fun delete_oneChild() {
		val tree = AVLTree(AVLNode(1, "one"))
		tree.insert(2, "two")
		tree.insert(3, "three")
		tree.insert(4, "four")
		val deleted = tree.delete(3)

		assertEquals(4, deleted?.key)
	}

	@Test
	@DisplayName("Delete: two children")
	fun delete_nodeWithTwoChildren_replacesWithPredecessor() {
		val tree = AVLTree<Int, String>(AVLNode(10, "ten"))
		tree.insert(5, "five")
		tree.insert(15, "fifteen")
		tree.insert(7, "seven")
		val deleted = tree.delete(10)
		assertNotNull(deleted)
		// Predecessor of 10 is 7
		assertEquals(7, tree.root?.key)
		assertEquals("seven", tree.root?.data)
	}

	@Test
	@DisplayName("Delete: nonexistent key")
	fun delete_nonexistentKey_returnsNullAndLeavesTreeUnchanged() {
		val tree = AVLTree<Int, String>(AVLNode(1, "one"))
		assertNull(tree.delete(42))
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
		iter.next()  // consume the only element
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
		// Trigger single left rotation
		tree.insert(1, "one")
		tree.insert(2, "two")
		tree.insert(3, "three")
		// Trigger left-right rotation
		tree.insert(5, "five")
		tree.insert(4, "four")
		// Trigger right-left rotation
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
}
