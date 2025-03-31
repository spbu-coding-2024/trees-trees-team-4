package org.treelib

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class BSTTest {

    @Test
    fun `min of empty tree`(){
        val tree: BST<Int, Int> = BST()
        assert(tree.findMin() == null)
    }
    @Test
    fun `max of empty tree`(){
        val tree: BST<Int, Int> = BST()
        assert(tree.findMax() == null)
    }
    @Test
    fun `insert to empty tree`(){
        val tree: BST<Int, Int> = BST()
        val node: BSTNode<Int, Int> = tree.insert(0, 0)
        assert(tree.root == node)
    }
    @Test
    fun `delete from empty tree`(){
        val tree: BST<Int, Int> = BST()
        assert(tree.delete(0) == null)
    }
    @Test
    fun `insert to single node tree (left)`(){
        val tree: BST<Int, Int> = BST(BSTNode(1, 1))
        val node = tree.insert(0, 0)
        assert(tree.root?.left == node)
    }
    @Test
    fun `insert to single node tree (right)`(){
        val tree: BST<Int, Int> = BST(BSTNode(1, 1))
        val node = tree.insert(2, 2)
        assert(tree.root?.right == node)
    }
    @Test
    fun `insert must update data`(){
        val tree: BST<Int, Int> = BST(BSTNode(1, 1))
        val node = tree.insert(1, 2)
        assert(tree.root?.data == node.data)
    }
    @Test
    fun `is delete from single node tree returning required node`(){
        val node: BSTNode<Int, Int> = BSTNode(1, 1)
        val tree: BST<Int, Int> = BST(node)
        assert(tree.delete(1) == node)
    }
    @Test
    fun `is delete from single node tree actually deleting required node`(){
        val node: BSTNode<Int, Int> = BSTNode(1, 1)
        val tree: BST<Int, Int> = BST(node)
        assert(tree.delete(1) == node)
    }
    @Test
    fun `simple min (int)`(){
        val tree: BST<Int, Int> = BST(BSTNode(1, 1))
        val node = tree.insert(0, 0)
        tree.insert(2, 2)
        assert(tree.findMin() == node)
    }

    @Test
    fun `simple max (int)`(){
        val tree: BST<Int, Int> = BST(BSTNode(1, 1))
        val node = tree.insert(2, 2)
        tree.insert(0, 0)
        assert(tree.findMax() == node)
    }
    @Test
    fun `simple min (string)`(){
        val tree: BST<String, Int> = BST(BSTNode("bobr", 0))
        val node = tree.insert("a", 0)
        tree.insert("k-wa", 2)
        assert(tree.findMin() == node)
    }

    @Test
    fun `simple max (string)`(){
        val tree: BST<String, Int> = BST(BSTNode("bobr", 0))
        val node = tree.insert("k-wa", 0)
        tree.insert("ja", 2)
        assert(tree.findMax() == node)
    }
    @Test
    fun `a lot of desending insert and min`(){
        val tree: BST<Int, Int> = BST(BSTNode(9, 0))
        tree.insert(8, 0)
        tree.insert(7, 0)
        tree.insert(6, 0)
        tree.insert(5, 0)
        tree.insert(4, 0)
        tree.insert(3, 0)
        tree.insert(2, 0)
        tree.insert(1, 0)
        tree.insert(0, 0)
        var node: BSTNode<Int, Int>? = tree.root
        for (i in 9 downTo 0) {
            assertEquals(i, node?.key)
            node = node?.left
        }
    }
    @Test
    fun `balanced inserting`(){
        val tree: BST<Int, Int> = BST(BSTNode(5, 0))
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
    fun `balanced inserting and replace root`(){
        val tree: BST<Int, Int> = BST(BSTNode(5, 0))
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
    fun `balanced inserting and replace mid (left)`(){
        val tree: BST<Int, Int> = BST(BSTNode(5, 0))
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
    fun `balanced inserting and replace mid (right)`(){
        val tree: BST<Int, Int> = BST(BSTNode(5, 0))
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
    fun `complex delete root`(){
        val tree: BST<Int, Int> = BST(BSTNode(5, 0))
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
    fun `complex delete mid(left)`(){
        val tree: BST<Int, Int> = BST(BSTNode(5, 0))
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
    fun `complex delete mid(right)`(){
        val tree: BST<Int, Int> = BST(BSTNode(5, 0))
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
    fun `tree structure after delete`(){
        val tree: BST<Int, Int> = BST(BSTNode(5, 0))
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
        assert(isIntTreeConsistent(tree.root))
    }
    private fun isIntTreeConsistent(root: BSTNode<Int, Int>?): Boolean{
        var result = false
        val left = root?.left
        val right = root?.right
        if (root == null)
            result = true
        else if (left == null && right == null) {
            result = true
        }
        else if (right != null && left != null) {
            result = left.key < root.key && root.key < right.key
                    && isIntTreeConsistent(right) && isIntTreeConsistent(left)
        }
        else if (right != null) {
            result = root.key < right.key && isIntTreeConsistent(right)
        }
        else if (left != null) {
            result = root.key > left.key && isIntTreeConsistent(left)
        }
        return result
    }
}
