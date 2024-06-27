package au.edu.anu.comp6442.group03.studyapp.treevisualizer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RBTreeVisualizationView extends View {
    private Paint paint = new Paint();
    private RBTree redBlackTree;

    //private NodeAndCoordinates nodeCoordPair;


    public RBTreeVisualizationView(Context context, RBTree redBlackTree) {
        super(context);
        this.redBlackTree = redBlackTree;
        //this.nodeCoordPair = nodeCoordPair;
        initialize();
    }

    private void initialize() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(40);
        paint.setStrokeWidth(5);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        drawRBTree(canvas, redBlackTree.root, getWidth() / 2, 100);

    }

    /**
     * @param canvas
     * @param currentNode
     * @param x
     * @param y           Note/disclaimer: This 'drawRBTree' method is similar to Yingjie's implementation of 'drawTree' method in BinaryTreeVisualizationView class but
     *                    stores the coordinates of every nodes in the red-black tree to be used to adjust the relative positioning between
     *                    parent and children nodes.
     */
    private void drawRBTree(Canvas canvas, RBTreeNode currentNode, float x, float y) {
        if (currentNode == null) {
            return;
        } else {
            CoordPair currentNodeCoordinate = redBlackTree.getCoord(currentNode.value);
            int currentNodeLevel = redBlackTree.getNodeLevel(currentNode.value);
            float xShift = 0;
            float yShift = 0;

            // Draw left subtree
            if (currentNode.left != null) {
                if (redBlackTree.getCoord(currentNode.left.value).getX() == 0 && redBlackTree.getCoord(currentNode.left.value).getY() == 0) {
                    int nodeLevel = currentNodeLevel + 1;
                    //CoordPair nodeCoordinate = new CoordPair(currentNodeCoordinate.getX() - 80, currentNodeCoordinate.getY() + 100);
                    xShift = currentNodeCoordinate.getX() - 80;
                    yShift = currentNodeCoordinate.getY() + 100;
                    // check in the current nodeLevel, whether there are any neighbours
                    // Check if any neighbour exists
                /*
                if (!getNeighbours(currentNode.left.value, nodeLevel).isEmpty()){
                    // check if there is neighbour with the same coordinate as this newly computed coordinate
                    int overlapNeighbour = getOverlapNeighbour(getNeighbours(currentNode.left.value, nodeLevel), new CoordPair(xShift, yShift));
                    // Check if any neighbour overlaps
                    if (overlapNeighbour > 0 && currentNode != redBlackTree.root){
                        redBlackTree.updateCoordinate(currentNode.value,
                                new LevelAndCoordinates(currentNodeLevel, new CoordPair(currentNodeCoordinate.getX() + 105, currentNodeCoordinate.getY())));
                    }
                    //adjust parent nodes until theres no overlap

                }

                 */
                    adjustAncestorsIfOverlap(currentNode.left, nodeLevel, xShift, yShift);
                    //adjustNodes(currentNode.left, nodeLevel, xShift, yShift);
                    CoordPair newCurrentNodeCoord = redBlackTree.getCoord(currentNode.value);
                    CoordPair nodeCoordinate = new CoordPair(newCurrentNodeCoord.getX() - 80, newCurrentNodeCoord.getY() + 100);
                    // update this left child level and coordinate
                    redBlackTree.updateCoordinate(currentNode.left.value,
                            new LevelAndCoordinates(nodeLevel, nodeCoordinate));
                }
                drawRBTree(canvas, currentNode.left, redBlackTree.getCoord(currentNode.left.value).getX(), redBlackTree.getCoord(currentNode.left.value).getY());

            }

            // Draw right subtree
            if (currentNode.right != null) {
                if (redBlackTree.getCoord(currentNode.right.value).getX() == 0 && redBlackTree.getCoord(currentNode.right.value).getY() == 0) {

                    int nodeLevel = currentNodeLevel + 1;
                    xShift = currentNodeCoordinate.getX() + 80;
                    yShift = currentNodeCoordinate.getY() + 100;

                /*
                if (!getNeighbours(currentNode.right.value, nodeLevel).isEmpty()){
                    // check if there is neighbour with the same coordinate as this newly computed coordinate
                    int overlapNeighbour = getOverlapNeighbour(getNeighbours(currentNode.right.value, nodeLevel), new CoordPair(xShift, yShift));
                    if (overlapNeighbour > 0 && currentNode != redBlackTree.root){
                        redBlackTree.updateCoordinate(currentNode.value,
                                new LevelAndCoordinates(currentNodeLevel, new CoordPair(currentNodeCoordinate.getX() - 105, currentNodeCoordinate.getY())));
                    }

                }

                 */
                    adjustAncestorsIfOverlap(currentNode.right, nodeLevel, xShift, yShift);
                    //adjustNodes(currentNode.right, nodeLevel, xShift, yShift);
                    CoordPair newCurrentNodeCoord = redBlackTree.getCoord(currentNode.value);
                    CoordPair nodeCoordinate = new CoordPair(newCurrentNodeCoord.getX() + 80, newCurrentNodeCoord.getY() + 100);

                    // update this right child level and coordinate
                    redBlackTree.updateCoordinate(currentNode.right.value,
                            new LevelAndCoordinates(nodeLevel, nodeCoordinate));
                }
                drawRBTree(canvas, currentNode.right, redBlackTree.getCoord(currentNode.right.value).getX(), redBlackTree.getCoord(currentNode.right.value).getY());
            }

            if (currentNode != redBlackTree.root) {
                paint.setColor(Color.BLACK);
                canvas.drawLine(redBlackTree.getCoord(currentNode.value).getX(), redBlackTree.getCoord(currentNode.value).getY(),
                        redBlackTree.getCoord(currentNode.parent.value).getX(), redBlackTree.getCoord(currentNode.parent.value).getY(),
                        paint);
            }

            // Drawing node circle
            if (redBlackTree.getCoord(currentNode.value).getX() == 0 && redBlackTree.getCoord(currentNode.value).getY() == 0) {
                redBlackTree.updateCoordinate(currentNode.value, new LevelAndCoordinates(0, new CoordPair(x, y)));
            }
            if (currentNode.colour == RBTreeNode.nodeColour.BLACK) {
                paint.setColor(Color.BLACK);
            } else {
                paint.setColor(Color.RED);
            }

            canvas.drawCircle(redBlackTree.getCoord(currentNode.value).getX(), redBlackTree.getCoord(currentNode.value).getY(), 35, paint);

            // Draw node value text
            paint.setColor(Color.WHITE);
            paint.setTextAlign(Paint.Align.CENTER);

            float textY = redBlackTree.getCoord(currentNode.value).getY() - ((paint.descent() + paint.ascent()) / 2);
            canvas.drawText(String.valueOf(currentNode.value), redBlackTree.getCoord(currentNode.value).getX(), textY, paint);


        }

    }

    private List<Integer> getNeighbours(int nodeValue, int nodeLevel){
        List<Integer> neighboursVal = new ArrayList<>();
        for (Map.Entry<Integer, LevelAndCoordinates> entry : redBlackTree.getNodeMapping().entrySet()){
            if (entry.getValue().getLevel() == nodeLevel && entry.getKey() != nodeValue){
                neighboursVal.add(entry.getKey());
            }
        }
        return neighboursVal;
    }

    private int getOverlapNeighbour(List<Integer> neighbours, CoordPair nodeCoordinate){
        for (int neighbour : neighbours){

            if (redBlackTree.getCoord(neighbour).getX() == nodeCoordinate.getX()
                    && redBlackTree.getCoord(neighbour).getY() == nodeCoordinate.getY()){
                return neighbour;
            }

        }
        return 0;
    }

    private void adjustAncestorsIfOverlap(RBTreeNode node, int nodeLevel, float xShift, float yShift){
        if (node.parent != null){
            CoordPair prjNodeCoordinate = new CoordPair(xShift, yShift);
            List<Integer> neighbors = getNeighbours(node.value, nodeLevel);

            CoordPair parentCoordinate = redBlackTree.getCoord(node.parent.value);
            int parentNodeLevel = redBlackTree.getNodeLevel(node.parent.value);
            for (int neighbor : neighbors){
                if (Math.abs(redBlackTree.getCoord(neighbor).getX() - prjNodeCoordinate.getX()) < 35
                        && redBlackTree.getCoord(neighbor).getY() == prjNodeCoordinate.getY()){
                    if (node.parent != redBlackTree.root) {
                        if (node == node.parent.left) {
                            redBlackTree.updateCoordinate(node.parent.value, new LevelAndCoordinates(parentNodeLevel,
                                    new CoordPair(parentCoordinate.getX() + 80, parentCoordinate.getY())));
                            if (node.parent.right != null && redBlackTree.getCoord(node.parent.right.value).getX() != 0 && redBlackTree.getCoord(node.parent.right.value).getY() != 0){
                                redBlackTree.updateCoordinate(node.parent.right.value,
                                        new LevelAndCoordinates(nodeLevel,
                                                new CoordPair(redBlackTree.getCoord(node.parent.right.value).getX() + 80,
                                                        redBlackTree.getCoord(node.parent.right.value).getY())));

                            }
                        }
                        else{
                            redBlackTree.updateCoordinate(node.parent.value, new LevelAndCoordinates(parentNodeLevel,
                                    new CoordPair(parentCoordinate.getX() - 80, parentCoordinate.getY())));
                            if (node.parent.left != null && redBlackTree.getCoord(node.parent.left.value).getX() != 0 && redBlackTree.getCoord(node.parent.left.value).getY() != 0){
                                redBlackTree.updateCoordinate(node.parent.left.value,
                                        new LevelAndCoordinates(nodeLevel,
                                                new CoordPair(redBlackTree.getCoord(node.parent.left.value).getX() - 80,
                                                        redBlackTree.getCoord(node.parent.left.value).getY())));

                            }

                        }
                    }
                }

            }
            adjustAncestorsIfOverlap(node.parent, nodeLevel - 1, redBlackTree.getCoord(node.parent.value).getX(), redBlackTree.getCoord(node.parent.value).getY());
        }
    }

    private void adjustNodes(RBTreeNode node, int nodeLevel, float xShift, float yShift){
        if (node.parent.parent != null){
            CoordPair prjNodeCoordinate = new CoordPair(xShift, yShift);
            CoordPair grandparentCoord = redBlackTree.getCoord(node.parent.parent.value);
            CoordPair parentCoordinate = redBlackTree.getCoord(node.parent.value);
            int parentNodeLevel = redBlackTree.getNodeLevel(node.parent.value);


            if (prjNodeCoordinate.getX() == grandparentCoord.getX()){
                //&& node.parent == node.parent.parent.right
                if (node == node.parent.left) {
                    redBlackTree.updateCoordinate(node.parent.value, new LevelAndCoordinates(parentNodeLevel,
                            new CoordPair(parentCoordinate.getX() + 80, parentCoordinate.getY())));

                    //if (node.parent.right != null && redBlackTree.getCoord(node.parent.right.value).getX() != 0 && redBlackTree.getCoord(node.parent.right.value).getY() != 0) {
                    if (node.parent.right != null){
                        redBlackTree.updateCoordinate(node.parent.right.value,
                                new LevelAndCoordinates(nodeLevel,
                                        new CoordPair(redBlackTree.getCoord(node.parent.value).getX() + 80,
                                                redBlackTree.getCoord(node.parent.value).getY() + 100)));


                    }
                }
                //&& node.parent == node.parent.parent.left
                else if (node == node.parent.right){
                    redBlackTree.updateCoordinate(node.parent.value, new LevelAndCoordinates(parentNodeLevel,
                            new CoordPair(parentCoordinate.getX() - 80, parentCoordinate.getY())));

                    //if (node.parent.left != null && redBlackTree.getCoord(node.parent.left.value).getX() != 0 && redBlackTree.getCoord(node.parent.left.value).getY() != 0){
                    if (node.parent.left != null){
                        redBlackTree.updateCoordinate(node.parent.left.value,
                                new LevelAndCoordinates(nodeLevel,
                                        new CoordPair(redBlackTree.getCoord(node.parent.value).getX() - 80,
                                                redBlackTree.getCoord(node.parent.value).getY() + 100)));

                    }


                }
            }
            adjustNodes(node.parent, parentNodeLevel, redBlackTree.getCoord(node.parent.value).getX(), redBlackTree.getCoord(node.parent.value).getY());

        }
    }


}
