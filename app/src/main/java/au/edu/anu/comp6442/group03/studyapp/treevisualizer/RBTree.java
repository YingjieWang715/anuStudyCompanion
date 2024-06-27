package au.edu.anu.comp6442.group03.studyapp.treevisualizer;

import java.util.HashMap;
import java.util.Stack;

public class RBTree {
    RBTreeNode root;

    NodeAndCoordinates nodeAndCoordinates;

    // Store constructed Tree nodes  -> similar to Yingjie's implementation in BinaryTree class
    Stack<Integer> addedRBTreeNodes = new Stack<>();


    public RBTree(NodeAndCoordinates nodeCoordinateMapping){
        this.nodeAndCoordinates = nodeCoordinateMapping;
    }

    public void updateCoordinate(int nodeValue, LevelAndCoordinates levelCoord){
        this.nodeAndCoordinates.updateNodeCoordinate(nodeValue, levelCoord);
    }

    public CoordPair getCoord(int nodeValue){
        return this.nodeAndCoordinates.getCoordinate(nodeValue);
    }

    public int getNodeLevel(int nodeValue){
        return this.nodeAndCoordinates.getLevel(nodeValue);
    }

    public int getRootValue(){
        return this.root.value;
    }

    public int getLeftChildofRoot(){
        return this.root.left.value;
    }

    public int getRightChildofRoot(){
        return this.root.right.value;
    }
    public HashMap<Integer, LevelAndCoordinates> getNodeMapping(){
        return this.nodeAndCoordinates.getNodeMap();
    }



    /**
     *
     * @param value
     * Note: this add function is similar to Yingjie's implementation of 'add' method in BinaryTree class.
     */
    public void add(int value){
        RBTreeNode newNode = new RBTreeNode(value);
        //root = addRecursive(root, value);
        root = addRecursive(root, newNode);
        handleColour(newNode);
        addedRBTreeNodes.push(newNode.value);

    }

    /**
     *
     * @param current
     * @param newNode
     * @return
     *
     * Note: this addRecursive function is similar to Yingjie's implementation of addRecursive in BinaryTree class but instead of an integer value, it
     * takes the newly created node (instance of RBTreeNode) as its 2nd parameter.
     */
    private RBTreeNode addRecursive(RBTreeNode current, RBTreeNode newNode){
        if (current == null){
            //RBTreeNode newNode = new RBTreeNode(value);

            return newNode;
        }

        if (newNode.value < current.value){
            current.left = addRecursive(current.left, newNode);
            current.left.parent = current;
            //handleColour(current.left);
        }

        else if (newNode.value > current.value){
            current.right = addRecursive(current.right, newNode);
            current.right.parent = current;
            //handleColour(current.right);
        }

        return current;
    }

    private void handleColour(RBTreeNode insertedNode){
        if (insertedNode == null || insertedNode == root){
            root.colour = RBTreeNode.nodeColour.BLACK;
            return;
        }

        if (insertedNode.parent.colour == RBTreeNode.nodeColour.BLACK){
            return;
        }
        // If grandparent exists
        if (insertedNode.parent.parent != null){
            if (insertedNode.parent == insertedNode.parent.parent.left){
                RBTreeNode uncle = insertedNode.parent.parent.right;

                //Case 1: uncle is red
                if (uncle != null && uncle.colour == RBTreeNode.nodeColour.RED){
                    insertedNode.parent.colour = RBTreeNode.nodeColour.BLACK;
                    uncle.colour = RBTreeNode.nodeColour.BLACK;
                    insertedNode.parent.parent.colour = RBTreeNode.nodeColour.RED;
                    handleColour(insertedNode.parent.parent);
                    return;
                } else{
                    // Case 2: uncle is black or null and inserted/current node is right child
                    if (insertedNode == insertedNode.parent.right){
                        insertedNode = insertedNode.parent;
                        leftRotate(insertedNode);
                    }
                    // Case 3: uncle is black or null and inserted/current node is left
                    insertedNode.parent.colour = RBTreeNode.nodeColour.BLACK;
                    insertedNode.parent.parent.colour = RBTreeNode.nodeColour.RED;
                    rightRotate(insertedNode.parent.parent);
                    return;
                }
            } else{ //parent of current/inserted node is right child
                RBTreeNode uncle = insertedNode.parent.parent.left;

                //Case 1: uncle is red, recolor the tree
                if (uncle != null && uncle.colour == RBTreeNode.nodeColour.RED){
                    insertedNode.parent.colour = RBTreeNode.nodeColour.BLACK;
                    uncle.colour = RBTreeNode.nodeColour.BLACK;
                    insertedNode.parent.parent.colour = RBTreeNode.nodeColour.RED;
                    handleColour(insertedNode.parent.parent);
                    return;
                } else{
                    //Case 2: uncle is black or null and current/inserted node is left child
                    if (insertedNode == insertedNode.parent.left){
                        insertedNode = insertedNode.parent;
                        rightRotate(insertedNode);
                    }
                    //Case 3: if uncle is black or null and current/inserted node is right child
                    insertedNode.parent.colour = RBTreeNode.nodeColour.BLACK;
                    insertedNode.parent.parent.colour = RBTreeNode.nodeColour.RED;
                    leftRotate(insertedNode.parent.parent);
                    return;
                }

            }
        }
    }

    private void leftRotate(RBTreeNode node){
        RBTreeNode newParent = node.right;
        node.right = newParent.left;
        if (newParent.left != null){
            newParent.left.parent = node;
        }

        newParent.parent = node.parent;
        if (node.parent == null){
            root = newParent;
        }
        else{
            if(node == node.parent.left){
                node.parent.left = newParent;
            }
            else if(node == node.parent.right){
                node.parent.right = newParent;
            }
        }
        newParent.left = node;
        node.parent = newParent;
        //CoordPair tmpCoord = getCoord(newParent.value);
        //int tmpLevel = getNodeLevel(newParent.value);
        //updateCoordinate(newParent.value, new LevelAndCoordinates(getNodeLevel(node.value), getCoord(node.value)));
        //updateCoordinate(node.value, new LevelAndCoordinates(tmpLevel,tmpCoord));
    }

    private void rightRotate(RBTreeNode node){
        RBTreeNode newParent = node.left;
        node.left = newParent.right;
        if(newParent.right != null){
            newParent.right.parent = node;
        }

        newParent.parent = node.parent;
        if (node.parent == null){
            root = newParent;
        }
        else{
            if(node == node.parent.right){
                node.parent.right = newParent;
            }
            else if(node == node.parent.left){
                node.parent.left = newParent;
            }
        }
        newParent.right = node;
        node.parent = newParent;
        // CoordPair tmpCoord = getCoord(newParent.value);
        // int tmpLevel = getNodeLevel(newParent.value);
        // updateCoordinate(newParent.value, new LevelAndCoordinates(getNodeLevel(node.value), getCoord(node.value)));
        // updateCoordinate(node.value, new LevelAndCoordinates(tmpLevel,tmpCoord));
    }

    public void clearRBTree(){
        root = null;
        addedRBTreeNodes.clear();
    }



}
