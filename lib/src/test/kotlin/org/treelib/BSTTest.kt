package org.treelib

import org.junit.jupiter.api.Test

class BSTTest {

    private val tree: BST<Int, Int> = BST()
    @Test
    fun `min of empty tree`(){
        assert(tree.findMin() == null)
    }
    @Test
    fun `max of empty tree`(){
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
    fun `is min of single node tree = max of single node tree`(){
        val tree: BST<Int, Int> = BST(BSTNode(0, 0))
        assert(tree.findMin() == tree.findMax())
    }
    @Test
    fun `insert to single node tree`(){
        val tree: BST<Int, Int> = BST(BSTNode(1, 1))
        val node = tree.insert(0, 0)
        assert(tree.root?.left == node)
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
        TODO("Not yet implemented")
    }

    @Test
    fun `simple max (int)`(){
        TODO("Not yet implemented")
    }

    @Test
    fun `simple min (string)`(){
        TODO("Not yet implemented")
    }

    @Test
    fun `simple max (string)`(){
        TODO("Not yet implemented")
    }

    @Test
    fun `simple min (Node Int, Int)`(){
        TODO("Not yet implemented")
    }

    @Test
    fun `simple max (Node Int, Int)`(){
        TODO("Not yet implemented")
    }

}
