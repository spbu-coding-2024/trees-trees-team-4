package org.treelib

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

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
        assert(tree.search(0)?.second == 0)
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
        assert(tree.search(0)?.second == 0)
        assert(tree.isConsistent())
    }

    @Test
    fun `insert to single node tree (right)`() {
        val tree: BinarySearchTree<Int, Int> = BinarySearchTree(1, 1)
        tree.insert(2, 2)
        assert(tree.search(2)?.second == 2)
        assert(tree.isConsistent())
    }

    @Test
    fun `insert must update data`() {
        val tree: BinarySearchTree<Int, Int> = BinarySearchTree(1, 1)
        tree.insert(1, 2)
        assert(tree.search(1)?.second == 2)
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
        val tree: BinarySearchTree<String, Int> = BinarySearchTree("bobr", 1)
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
        for (i in 9 downTo 0) {
            tree.insert(i, i)
        }
        for (i in 9 downTo 0) {
            assertEquals(i, tree.search(i)?.second)
        }
        assert(tree.findMin() == 0)
    }

    @Test
    fun `balanced inserting`() {
        val tree: BinarySearchTree<Int, Int> = BinarySearchTree(5, 5)
        tree.insert(7, 7)
        tree.insert(8, 8)
        tree.insert(6, 6)
        tree.insert(9, 9)
        tree.insert(2, 2)
        tree.insert(1, 1)
        tree.insert(4, 4)
        tree.insert(3, 3)
        tree.insert(0, 0)
        assert(tree.isConsistent())
        for (i in tree) {
            assert(tree.search(i.first)?.second == i.second)
        }
    }

    @Test
    fun `balanced inserting and replace root`() {
        val tree: BinarySearchTree<Int, Int> = BinarySearchTree(5, 5)
        tree.insert(7, 7)
        tree.insert(8, 8)
        tree.insert(6, 6)
        tree.insert(9, 9)
        tree.insert(2, 2)
        tree.insert(1, 1)
        tree.insert(4, 4)
        tree.insert(3, 3)
        tree.insert(0, 0)
        tree.insert(5, -5)
        assert(tree.isConsistent())
        for (i in 0..9) {
            if (i == 5) {
                assert(tree.search(i)?.second == -5)
            } else {
                assert(tree.search(i)?.second == i)
            }
        }
    }

    @Test
    fun `balanced inserting and replace mid (left)`() {
        val tree: BinarySearchTree<Int, Int> = BinarySearchTree(5, 5)
        tree.insert(7, 7)
        tree.insert(8, 8)
        tree.insert(6, 6)
        tree.insert(9, 9)
        tree.insert(2, 2)
        tree.insert(1, 1)
        tree.insert(4, 4)
        tree.insert(3, 3)
        tree.insert(0, 0)
        tree.insert(2, -2)
        assert(tree.isConsistent())
        for (i in 0..9) {
            if (i == 2) {
                assert(tree.search(i)?.second == -2)
            } else {
                assert(tree.search(i)?.second == i)
            }
        }
    }

    @Test
    fun `balanced inserting and replace mid (right)`() {
        val tree: BinarySearchTree<Int, Int> = BinarySearchTree(5, 5)
        tree.insert(7, 7)
        tree.insert(8, 8)
        tree.insert(6, 6)
        tree.insert(9, 9)
        tree.insert(2, 2)
        tree.insert(1, 1)
        tree.insert(4, 4)
        tree.insert(3, 3)
        tree.insert(0, 0)
        tree.insert(7, -7)
        assert(tree.isConsistent())
        for (i in 0..9) {
            if (i == 7) {
                assert(tree.search(i)?.second == -7)
            } else {
                assert(tree.search(i)?.second == i)
            }
        }
    }

    @Test
    fun `complex delete root`() {
        val tree: BinarySearchTree<Int, Int> = BinarySearchTree(5, 5)
        tree.insert(7, 7)
        tree.insert(8, 8)
        tree.insert(6, 6)
        tree.insert(9, 9)
        tree.insert(2, 2)
        tree.insert(1, 1)
        tree.insert(4, 4)
        tree.insert(3, 3)
        tree.insert(0, 0)
        tree.delete(5)
        assert(tree.isConsistent())
        for (i in 0..9) {
            if (i == 5) {
                assert(tree.search(i) == null)
            } else {
                assert(tree.search(i)?.second == i)
            }
        }
    }

    @Test
    fun `complex delete mid(left)`() {
        val tree: BinarySearchTree<Int, Int> = BinarySearchTree(5, 5)
        tree.insert(7, 7)
        tree.insert(8, 8)
        tree.insert(6, 6)
        tree.insert(9, 9)
        tree.insert(2, 2)
        tree.insert(1, 1)
        tree.insert(4, 4)
        tree.insert(3, 3)
        tree.insert(0, 0)
        tree.delete(2)
        assert(tree.isConsistent())
        for (i in 0..9) {
            if (i == 2) {
                assert(tree.search(i) == null)
            } else {
                assert(tree.search(i)?.second == i)
            }
        }
    }

    @Test
    fun `complex delete mid(right)`() {
        val tree: BinarySearchTree<Int, Int> = BinarySearchTree(5, 5)
        tree.insert(7, 7)
        tree.insert(8, 8)
        tree.insert(6, 6)
        tree.insert(9, 9)
        tree.insert(2, 2)
        tree.insert(1, 1)
        tree.insert(4, 4)
        tree.insert(3, 3)
        tree.insert(0, 0)
        tree.delete(7)
        assert(tree.isConsistent())
        for (i in 0..9) {
            if (i == 7) {
                assert(tree.search(i) == null)
            } else {
                assert(tree.search(i)?.second == i)
            }
        }
    }
}
