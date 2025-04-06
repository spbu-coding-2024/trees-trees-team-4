package org.treelib

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
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
        tree.insert(0, 0)
        assert(tree.root?.data == 0)
        assert(tree.root?.key == 0)
    }
    @Test
    fun `delete from empty tree`(){
        val tree: BST<Int, Int> = BST()
        assert(tree.delete(0) == null)
    }
    @Test
    fun `insert to single node tree (left)`(){
        val tree: BST<Int, Int> = BST(BSTNode(1, 1))
        tree.insert(0, 0)
        assert(tree.root?.left?.data == 0)
        assert(tree.root?.left?.key == 0)
    }
    @Test
    fun `insert to single node tree (right)`(){
        val tree: BST<Int, Int> = BST(BSTNode(1, 1))
        tree.insert(2, 2)
        assert(tree.root?.right?.data == 2)
        assert(tree.root?.right?.key == 2)
    }
    @Test
    fun `insert must update data`(){
        val tree: BST<Int, Int> = BST(BSTNode(1, 1))
        val node = tree.insert(1, 2)
        assert(tree.root?.data == 2)
        assert(tree.root?.key == 1)
    }
    @Test
    fun `is delete from single node tree returning required node`(){
        val node: BSTNode<Int, Int> = BSTNode(1, 1)
        val tree: BST<Int, Int> = BST(node)
        assert(tree.delete(1) == node.data)
    }
    @Test
    fun `is delete from single node tree actually deleting required node`(){
        val node: BSTNode<Int, Int> = BSTNode(1, 1)
        val tree: BST<Int, Int> = BST(node)
        assert(tree.delete(1) == node.data)
    }
    @Test
    fun `simple min (int)`(){
        val tree: BST<Int, Int> = BST(BSTNode(1, 1))
        tree.insert(0, 0)
        tree.insert(2, 2)
        assert(tree.findMin()?.data == 0)
        assert(tree.findMin()?.key == 0)
    }

    @Test
    fun `simple max (int)`(){
        val tree: BST<Int, Int> = BST(BSTNode(1, 1))
        tree.insert(2, 2)
        tree.insert(0, 0)
        assert(tree.findMin()?.data == 0)
        assert(tree.findMin()?.key == 0)
    }
    @Test
    fun `simple min (string)`(){
        val tree: BST<String, Int> = BST(BSTNode("bobr", 0))
        tree.insert("a", 0)
        tree.insert("k-wa", 2)
        assert(tree.findMin()?.data == 0)
        assert(tree.findMin()?.key == "a")
    }

    @Test
    fun `simple max (string)`(){
        val tree: BST<String, Int> = BST(BSTNode("bobr", 0))
        tree.insert("k-wa", 0)
        tree.insert("ja", 2)
        assert(tree.findMax()?.data == 0)
        assert(tree.findMax()?.key == "k-wa")
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

    @Test
    fun `iterator (int)`() {
        val tree: BST<Int, Int> = BST()
        val keys = arrayOf(13, 26, 89 ,36 , 15)
        val dataValues = arrayOf(0, 2 ,4 ,3, 1)
        for (i in 0..(keys.size - 1)) {
            tree.insert(keys[i], dataValues[i])
        }
        var i = 0
        tree.iterator().forEach {
            assertEquals(it, i)
            i++
        }
    }

    @Test
    fun `iterator (string)`() {
        val tree: BST<String, Int> = BST()
        val keys = arrayOf("never", "argue", "with", "stupid", "people")
        val dataValues = arrayOf(1, 0, 4, 3, 2)
        for (i in 0..(keys.size - 1)) {
            tree.insert(keys[i], dataValues[i])
        }
        var i = 0
        tree.iterator().forEach {
            assertEquals(it, i)
            i++
        }
    }
    @Test
    fun `next method test`() {
        val tree: BST<Int, Int> = BST()
        tree.insert(7, 123)
        tree.insert(8, 4)
        tree.insert(6, 2)
        tree.insert(9, 0)
        tree.insert(2, 123)
        tree.insert(1, 2)
        tree.insert(4, 213)
        tree.insert(3, 99)
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
