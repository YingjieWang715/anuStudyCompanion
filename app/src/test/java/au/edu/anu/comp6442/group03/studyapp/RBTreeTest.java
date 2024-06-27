package au.edu.anu.comp6442.group03.studyapp;

import org.junit.Test;
import static org.junit.Assert.*;
import au.edu.anu.comp6442.group03.studyapp.treevisualizer.NodeAndCoordinates;
import au.edu.anu.comp6442.group03.studyapp.treevisualizer.RBTree;

public class RBTreeTest {
    public RBTreeTest() {}

    @Test
    public void testInsertBasic(){
        NodeAndCoordinates nodeCoordMapping = new NodeAndCoordinates();
        RBTree newRBTree = new RBTree(nodeCoordMapping);
        newRBTree.add(32);
        newRBTree.add(24);
        newRBTree.add(41);


        assertEquals(32, newRBTree.getRootValue());
        assertEquals(24, newRBTree.getLeftChildofRoot());
        assertEquals(41, newRBTree.getRightChildofRoot());
    }

}
