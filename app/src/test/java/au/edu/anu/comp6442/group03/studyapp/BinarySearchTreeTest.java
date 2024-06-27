package au.edu.anu.comp6442.group03.studyapp;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import au.edu.anu.comp6442.group03.studyapp.note.data.BinarySearchTree;

public class BinarySearchTreeTest {
    private BinarySearchTree<Integer> bst;

    @Before
    public void setUp() {
        bst = new BinarySearchTree<>();
    }

    @Test
    public void testEmptyTree() {
        assertNull("Empty tree should have no root", bst.search(10));
    }

    @Test
    public void testInsert() {
        bst.insert(10);
        assertNotNull("Should find 10 in the tree", bst.search(10));
    }

    @Test
    public void testContains() {
        bst.insert(15);
        assertTrue("Should contain 15", bst.contains(15));
        assertFalse("Should not contain 20", bst.contains(20));
    }

    @Test
    public void testInsertAndSearch() {
        bst.insert(20);
        bst.insert(10);
        bst.insert(30);
        assertNotNull("Search should find 30 in the tree", bst.search(30));
    }

    @Test
    public void testDeleteLeaf() {
        bst.insert(20);
        bst.insert(10);
        bst.insert(30);
        bst.delete(10);
        assertNull("Leaf node 10 should be deleted", bst.search(10));
    }

    @Test
    public void testDeleteNodeWithOneChild() {
        bst.insert(20);
        bst.insert(10);
        bst.insert(30);
        bst.insert(25);
        bst.delete(30);
        assertNull("Node with one child (30) should be deleted", bst.search(30));
        assertNotNull("Child of deleted node (25) should remain", bst.search(25));
    }

    @Test
    public void testDeleteNodeWithTwoChildren() {
        bst.insert(20);
        bst.insert(10);
        bst.insert(30);
        bst.insert(25);
        bst.insert(35);
        bst.delete(30);
        assertNotNull("Node 30 replaced with 35 should still be found", bst.search(35));
        assertNull("Original node 30 should be deleted", bst.search(30));
    }

    @Test
    public void testInorderTraversal() {
        bst.insert(40);
        bst.insert(20);
        bst.insert(10);
        bst.insert(30);
        bst.insert(60);
        bst.insert(50);
        bst.insert(70);

        Iterator<Integer> it = bst.iterator();
        assertTrue(it.hasNext());
        assertEquals("Should return 10, the smallest element", (Integer) 10, it.next());
        assertEquals("Next in order should be 20", (Integer) 20, it.next());
    }

    @Test(expected = NoSuchElementException.class)
    public void testIteratorThrowsException() {
        bst.insert(20);
        Iterator<Integer> it = bst.iterator();
        it.next(); // should return 20
        it.next(); // should throw NoSuchElementException
    }
}
