package au.edu.anu.comp6442.group03.studyapp.treevisualizer;

import java.util.Stack;

public class BinaryTree {
    TreeNode root;
    // a stack to store all the nodes
    Stack<Integer> addedTreeNodes = new Stack<>();

    public BinaryTree() {
        root = null;
    }

    // add node
    public void add(int value) {

        root = addRecursive(root, value);
        addedTreeNodes.push(value);
    }

    private TreeNode addRecursive(TreeNode current, int value) {
        if (current == null) {
            return new TreeNode(value);
        }


        if (value < current.value) {
            current.left = addRecursive(current.left, value);
        } else if (value > current.value) {
            current.right = addRecursive(current.right, value);
        } else {
            // value already exists
            return current;
        }



        return current;
    }


    //  to calculate the max depth of the tree
    public int maxDepth(TreeNode node) {
        if (node == null) {
            return 0;
        } else {
            int leftDepth = maxDepth(node.left);
            int rightDepth = maxDepth(node.right);
            return Math.max(leftDepth, rightDepth) + 1;
        }
    }

    // delete a  specific node
    public void delete(int value) {
        root = deleteRecursive(root, value);
    }

    private TreeNode deleteRecursive(TreeNode current, int value) {
        if (current == null) {
            return null;
        }

        if (value == current.value) {
            // Node to delete is found

            // Case 1: no children
            if (current.left == null && current.right == null) {
                return null;
            }

            // Case 2: one child
            if (current.left == null) {
                return current.right;
            }
            if (current.right == null) {
                return current.left;
            }

            // Case 3: two children
            int smallestValue = findSmallestValue(current.right);
            current.value = smallestValue;
            current.right = deleteRecursive(current.right, smallestValue);
            return current;
        }
        if (value < current.value) {
            current.left = deleteRecursive(current.left, value);
        } else {
            current.right = deleteRecursive(current.right, value);
        }
        return current;
    }


    private int findSmallestValue(TreeNode root) {
        return root.left == null ? root.value : findSmallestValue(root.left);
    }


    public void clearTree() {
        root = null;
        addedTreeNodes.clear();
    }
    public void removeLastAdd() {
        if (!addedTreeNodes.isEmpty()) {
            int lastAddedValue = addedTreeNodes.pop();
            delete(lastAddedValue); // use the predefined delete method
        }
    }


}