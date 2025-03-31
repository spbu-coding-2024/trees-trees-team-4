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
        assert(tree.findMax() == null)
    }
    @Test
    fun `delete from empty tree`(){
        val tree: BST<Int, Int> = BST()
        assert(tree.findMax() == null)
    }
    @Test
    fun `is min of single node tree = max of single node tree`(){
        val tree: BST<Int, Int> = BST(BSTNode(0, 0))
        assert(tree.findMin() == tree.findMax())
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
