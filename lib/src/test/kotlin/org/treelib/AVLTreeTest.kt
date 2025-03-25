package org.treelib

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestReporter

class AVLTreeTest {

	@BeforeEach
	fun setUp() {
	}

	@AfterEach
	fun tearDown() {
	}

	@Test
	@DisplayName("Insert: root")
	fun insert_root(){
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
}