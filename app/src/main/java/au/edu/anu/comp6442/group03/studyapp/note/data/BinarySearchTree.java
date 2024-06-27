package au.edu.anu.comp6442.group03.studyapp.note.data;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

public class BinarySearchTree<T extends Comparable<T>> implements Iterable<T>, Serializable {
    private static final long serialVersionUID = 1L;
    @Expose
    private Node<T> root;

    private static class Node<T> implements Serializable {
        private static final long serialVersionUID = 1L;
        @Expose
        T key;
        @Expose
        Node<T> left, right;

        public Node(T item) {
            key = item;
            left = right = null;
        }
    }

    public BinarySearchTree() {
        root = null;
    }

    // Insert a new key into the BST
    public void insert(T key) {
        root = insertRec(root, key);
    }

    private Node<T> insertRec(Node<T> root, T key) {
        if (root == null) {
            root = new Node<>(key);
            return root;
        }

        if (key.compareTo(root.key) < 0) {
            root.left = insertRec(root.left, key);
        } else if (key.compareTo(root.key) > 0) {
            root.right = insertRec(root.right, key);
        }

        return root;
    }

    // This method mainly calls InorderRec()
    public void inorder() {
        inorderRec(root);
    }

    private void inorderRec(Node<T> root) {
        if (root != null) {
            inorderRec(root.left);
            System.out.print(root.key + " ");
            inorderRec(root.right);
        }
    }

    // A utility function to search a given key in BST
    public Node<T> search(T key) {
        return searchRec(root, key);
    }

    private Node<T> searchRec(Node<T> root, T key) {
        if (root == null || root.key.equals(key))
            return root;

        if (root.key.compareTo(key) > 0)
            return searchRec(root.left, key);

        return searchRec(root.right, key);
    }

    public void delete(T key) {
        root = deleteRec(root, key);
    }

    private Node<T> deleteRec(Node<T> root, T key) {
        // Base Case: If the tree is empty
        if (root == null) return root;

        // Otherwise, recur down the tree
        if (key.compareTo(root.key) < 0)
            root.left = deleteRec(root.left, key);
        else if (key.compareTo(root.key) > 0)
            root.right = deleteRec(root.right, key);
        else {
            // node with only one child or no child
            if (root.left == null)
                return root.right;
            else if (root.right == null)
                return root.left;

            // node with two children: Get the inorder successor (smallest in the right subtree)
            root.key = minValue(root.right);

            // Delete the inorder successor
            root.right = deleteRec(root.right, root.key);
        }

        return root;
    }

    T minValue(Node<T> root) {
        T minv = root.key;
        while (root.left != null) {
            minv = root.left.key;
            root = root.left;
        }
        return minv;
    }

    public boolean contains(T key) {
        return containsRec(root, key);
    }

    private boolean containsRec(Node<T> root, T key) {
        if (root == null) return false;
        if (key.equals(root.key)) return true;

        return key.compareTo(root.key) < 0 ? containsRec(root.left, key) : containsRec(root.right, key);
    }

    public Iterator<T> iterator() {
        return new BSTIterator(root);
    }

    private class BSTIterator implements Iterator<T> {
        private Stack<Node<T>> stack = new Stack<>();

        public BSTIterator(Node<T> root) {
            pushAll(root);
        }

        private void pushAll(Node<T> node) {
            for (; node != null; stack.push(node), node = node.left) ;
        }

        public boolean hasNext() {
            return !stack.isEmpty();
        }

        public T next() {
            if (!hasNext()) throw new NoSuchElementException();
            Node<T> node = stack.pop();
            pushAll(node.right);
            return node.key;
        }
    }
}