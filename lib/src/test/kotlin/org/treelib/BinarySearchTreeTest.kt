package org.treelib

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class BinarySearchTreeTest {

    @Test
    fun `min of empty tree`() {
        val tree: BinarySearchTree<Int, Int> = BinarySearchTree()
        assert(tree.findMin() == null)
    }

    @Test
    fun `max of empty tree`() {
        val tree: BinarySearchTree<Int, Int> = BinarySearchTree()
        assert(tree.findMax() == null)
    }

    @Test
    fun `insert to empty tree`() {
        val tree: BinarySearchTree<Int, Int> = BinarySearchTree()
        tree.insert(0, 0)
        assert(tree.search(0) == 0)
        assert(tree.search(0) == 0)
        assert(tree.isConsistent())
    }

    @Test
    fun `delete from empty tree`() {
        val tree: BinarySearchTree<Int, Int> = BinarySearchTree()
        try {
            tree.delete(0)
        } catch (e: NoSuchElementException) {
            assertEquals(e.message, "Cannot find node to be deleted")
        }
    }

    @Test
    fun `insert to single node tree (left)`() {
        val tree: BinarySearchTree<Int, Int> = BinarySearchTree(1, 1)
        tree.insert(0, 0)
        assert(tree.search(0) == 0)
        assert(tree.isConsistent())
    }

    @Test
    fun `insert to single node tree (right)`() {
        val tree: BinarySearchTree<Int, Int> = BinarySearchTree(1, 1)
        tree.insert(2, 2)
        assert(tree.search(2) == 2)
        assert(tree.isConsistent())
    }

    @Test
    fun `insert must update data`() {
        val tree: BinarySearchTree<Int, Int> = BinarySearchTree(1, 1)
        tree.insert(1, 2)
        assert(tree.search(1) == 2)
    }

    @Test
    fun `is delete from single node tree returning required node`() {
        val tree: BinarySearchTree<Int, Int> = BinarySearchTree(1, 1)
        assert(tree.delete(1) == 1)
    }

    @Test
    fun `is delete from single node tree actually deleting required node`() {
        val tree: BinarySearchTree<Int, Int> = BinarySearchTree(1, 1)
        tree.delete(1)
        assert(tree.search(1) == null)
    }

    @Test
    fun `simple min (int)`() {
        val tree: BinarySearchTree<Int, Int> = BinarySearchTree(1, 1)
        tree.insert(0, 0)
        tree.insert(2, 2)
        assert(tree.findMin() == 0)
    }

    @Test
    fun `simple max (int)`() {
        val tree: BinarySearchTree<Int, Int> = BinarySearchTree(1, 1)
        tree.insert(2, 2)
        tree.insert(0, 0)
        assert(tree.findMax() == 2)
    }

    @Test
    fun `simple min (string)`() {
        val tree: BinarySearchTree<String, Int> = BinarySearchTree("bobr", 0)
        tree.insert("a", 0)
        tree.insert("k-wa", 2)
        assert(tree.findMin() == 0)
    }

    @Test
    fun `simple max (string)`() {
        val tree: BinarySearchTree<String, Int> = BinarySearchTree("bobr", 1)
        tree.insert("k-wa", 0)
        tree.insert("ja", 2)
        assert(tree.findMax() == 0)
    }

    @Test
    fun `a lot of desending insert and min`() {
        val tree: BinarySearchTree<Int, Int> = BinarySearchTree(9, 0)
        for (i in 8 downTo 0) {
            tree.insert(i, i)
        }
        var node: BSTNode<Int, Int?>? = tree.root
        for (i in 9 downTo 0) {
            assertEquals(i, node?.key)
            node = node?.left
        }
    }

    @Test
    fun `balanced inserting`() {
        val tree: BinarySearchTree<Int, Int> = BinarySearchTree(5,0)
        tree.insert(7, 0)
        tree.insert(8, 0)
        tree.insert(6, 0)
        tree.insert(9, 0)
        tree.insert(2, 0)
        tree.insert(1, 0)
        tree.insert(4, 0)
        tree.insert(3, 0)
        tree.insert(0, 0)
        assertEquals(5, tree.root?.key)
        assertEquals(7, tree.root?.right?.key)
        assertEquals(8, tree.root?.right?.right?.key)
        assertEquals(6, tree.root?.right?.left?.key)
        assertEquals(9, tree.root?.right?.right?.right?.key)
        assertEquals(2, tree.root?.left?.key)
        assertEquals(1, tree.root?.left?.left?.key)
        assertEquals(4, tree.root?.left?.right?.key)
        assertEquals(3, tree.root?.left?.right?.left?.key)
        assertEquals(0, tree.root?.left?.left?.left?.key)
    }

    @Test
    fun `balanced inserting and replace root`() {
        val tree: BinarySearchTree<Int, Int> = BinarySearchTree(5, 0)
        tree.insert(7, 0)
        tree.insert(8, 0)
        tree.insert(6, 0)
        tree.insert(9, 0)
        tree.insert(2, 0)
        tree.insert(1, 0)
        tree.insert(4, 0)
        tree.insert(3, 0)
        tree.insert(0, 0)
        tree.insert(5, 5)
        assertEquals(5, tree.root?.key)
        assertEquals(5, tree.root?.data)
        assertEquals(7, tree.root?.right?.key)
        assertEquals(8, tree.root?.right?.right?.key)
        assertEquals(6, tree.root?.right?.left?.key)
        assertEquals(9, tree.root?.right?.right?.right?.key)
        assertEquals(2, tree.root?.left?.key)
        assertEquals(1, tree.root?.left?.left?.key)
        assertEquals(4, tree.root?.left?.right?.key)
        assertEquals(3, tree.root?.left?.right?.left?.key)
        assertEquals(0, tree.root?.left?.left?.left?.key)
    }

    @Test
    fun `balanced inserting and replace mid (left)`() {
        val tree: BinarySearchTree<Int, Int> = BinarySearchTree(5, 0)
        tree.insert(7, 0)
        tree.insert(8, 0)
        tree.insert(6, 0)
        tree.insert(9, 0)
        tree.insert(2, 0)
        tree.insert(1, 0)
        tree.insert(4, 0)
        tree.insert(3, 0)
        tree.insert(0, 0)
        tree.insert(2, 2)
        assertEquals(5, tree.root?.key)
        assertEquals(7, tree.root?.right?.key)
        assertEquals(8, tree.root?.right?.right?.key)
        assertEquals(6, tree.root?.right?.left?.key)
        assertEquals(9, tree.root?.right?.right?.right?.key)
        assertEquals(2, tree.root?.left?.key)
        assertEquals(2, tree.root?.left?.data)
        assertEquals(1, tree.root?.left?.left?.key)
        assertEquals(4, tree.root?.left?.right?.key)
        assertEquals(3, tree.root?.left?.right?.left?.key)
        assertEquals(0, tree.root?.left?.left?.left?.key)
    }

    @Test
    fun `balanced inserting and replace mid (right)`() {
        val tree: BinarySearchTree<Int, Int> = BinarySearchTree(5, 0)
        tree.insert(7, 0)
        tree.insert(8, 0)
        tree.insert(6, 0)
        tree.insert(9, 0)
        tree.insert(2, 0)
        tree.insert(1, 0)
        tree.insert(4, 0)
        tree.insert(3, 0)
        tree.insert(0, 0)
        tree.insert(7, 7)
        assertEquals(5, tree.root?.key)
        assertEquals(7, tree.root?.right?.key)
        assertEquals(7, tree.root?.right?.data)
        assertEquals(8, tree.root?.right?.right?.key)
        assertEquals(6, tree.root?.right?.left?.key)
        assertEquals(9, tree.root?.right?.right?.right?.key)
        assertEquals(2, tree.root?.left?.key)
        assertEquals(1, tree.root?.left?.left?.key)
        assertEquals(4, tree.root?.left?.right?.key)
        assertEquals(3, tree.root?.left?.right?.left?.key)
        assertEquals(0, tree.root?.left?.left?.left?.key)
    }

    @Test
    fun `complex delete root`() {
        val tree: BinarySearchTree<Int, Int> = BinarySearchTree(5, 0)
        tree.insert(7, 0)
        tree.insert(8, 0)
        tree.insert(6, 0)
        tree.insert(9, 0)
        tree.insert(2, 0)
        tree.insert(1, 0)
        tree.insert(4, 0)
        tree.insert(3, 0)
        tree.insert(0, 0)
        tree.delete(5)
        assertNotNull(tree.search(9))
        assertNotNull(tree.search(8))
        assertNotNull(tree.search(7))
        assertNotNull(tree.search(6))
        assertNull(tree.search(5))
        assertNotNull(tree.search(4))
        assertNotNull(tree.search(3))
        assertNotNull(tree.search(2))
        assertNotNull(tree.search(1))
        assertNotNull(tree.search(0))
    }

    @Test
    fun `complex delete mid(left)`() {
        val tree: BinarySearchTree<Int, Int> = BinarySearchTree(5, 0)
        tree.insert(7, 0)
        tree.insert(8, 0)
        tree.insert(6, 0)
        tree.insert(9, 0)
        tree.insert(2, 0)
        tree.insert(1, 0)
        tree.insert(4, 0)
        tree.insert(3, 0)
        tree.insert(0, 0)
        tree.delete(2)
        assertNotNull(tree.search(9))
        assertNotNull(tree.search(8))
        assertNotNull(tree.search(7))
        assertNotNull(tree.search(6))
        assertNotNull(tree.search(5))
        assertNotNull(tree.search(4))
        assertNotNull(tree.search(3))
        assertNull(tree.search(2))
        assertNotNull(tree.search(1))
        assertNotNull(tree.search(0))
    }

    @Test
    fun `complex delete mid(right)`() {
        val tree: BinarySearchTree<Int, Int> = BinarySearchTree(5, 0)
        tree.insert(7, 0)
        tree.insert(8, 0)
        tree.insert(6, 0)
        tree.insert(9, 0)
        tree.insert(2, 0)
        tree.insert(1, 0)
        tree.insert(4, 0)
        tree.insert(3, 0)
        tree.insert(0, 0)
        tree.delete(7)
        assertNotNull(tree.search(9))
        assertNotNull(tree.search(8))
        assertNull(tree.search(7))
        assertNotNull(tree.search(6))
        assertNotNull(tree.search(5))
        assertNotNull(tree.search(4))
        assertNotNull(tree.search(3))
        assertNotNull(tree.search(2))
        assertNotNull(tree.search(1))
        assertNotNull(tree.search(0))
    }

    @Test
    fun `tree structure after delete`() {
        val tree: BinarySearchTree<Int, Int> = BinarySearchTree(5, 0)
        tree.insert(7, 0)
        tree.insert(8, 0)
        tree.insert(6, 0)
        tree.insert(9, 0)
        tree.insert(2, 0)
        tree.insert(1, 0)
        tree.insert(4, 0)
        tree.insert(3, 0)
        tree.insert(0, 0)
        tree.delete(7)
        assert(tree.isConsistent())
    }
}
