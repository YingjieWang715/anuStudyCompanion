package au.edu.anu.comp6442.group03.studyapp.treevisualizer;

public class RBTreeNode {
    int value;
    public nodeColour colour;
    RBTreeNode left;
    RBTreeNode right;

    RBTreeNode parent;

    RBTreeNode(int value) {
        this.value = value;
        this.left = null;
        this.right = null;
        this.parent = null;
        this.colour = nodeColour.RED;

    }

    public enum nodeColour{
        BLACK, RED;
    }

}
