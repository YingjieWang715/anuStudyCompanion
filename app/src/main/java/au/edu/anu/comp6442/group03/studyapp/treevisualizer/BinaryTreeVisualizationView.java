package au.edu.anu.comp6442.group03.studyapp.treevisualizer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class BinaryTreeVisualizationView extends View {
    private Paint paint = new Paint();
    private BinaryTree binaryTree;


    public BinaryTreeVisualizationView(Context context, BinaryTree binaryTree) {
        super(context);
        this.binaryTree = binaryTree;
        initialize();
    }

    private void initialize() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(40);
        paint.setStrokeWidth(5);
    }

    @Override
    //This is the other implementation;
    /*
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int maxDepth = binaryTree.maxDepth(binaryTree.root);
        drawTree(canvas, binaryTree.root, getWidth() / 2, 100, getWidth() / 4, 1, maxDepth);
    }

    private void drawTree(Canvas canvas, TreeNode node, float x, float y, float horizontalDistance, int currentDepth, int maxDepth) {
        if (node == null) return;

        float newHorizontaldistance = horizontalDistance * (currentDepth / (float) maxDepth);

        if (node.left != null) {
            canvas.drawLine(x, y, x - newHorizontaldistance, y + 100, paint);
            drawTree(canvas, node.left, x - newHorizontaldistance, y + 100, horizontalDistance, currentDepth + 1, maxDepth);
        }
        if (node.right != null) {
            canvas.drawLine(x, y, x + newHorizontaldistance, y + 100, paint);
            drawTree(canvas, node.right, x + newHorizontaldistance, y + 100, horizontalDistance, currentDepth + 1, maxDepth);
        }
        // Draw the node circle
        paint.setColor(Color.BLACK); // Node circle color
        canvas.drawCircle(x, y, 30, paint);

        // Draw the node value
        paint.setColor(Color.WHITE); // Text color
        paint.setTextAlign(Paint.Align.CENTER); // Align text in the middle
        float textY = y - ((paint.descent() + paint.ascent()) / 2); // Adjust text position to center vertically
        canvas.drawText(String.valueOf(node.value), x, textY, paint);
*/


    protected void onDraw(Canvas canvas) {
        // Start drawing
        drawTree(canvas, binaryTree.root, getWidth() / 2, 100, getWidth() / 4);
    }

    private void drawTree(Canvas canvas, TreeNode node, float x, float y, float horizontalDistance) {

        if (node == null) return;

        // Reduce the horizontal spacing for each level to fit into the view
        horizontalDistance *= 0.5;

        // Draw left
        if (node.left != null) {
            canvas.drawLine(x, y, x - horizontalDistance, y + 100, paint);
            drawTree(canvas, node.left, x - horizontalDistance, y + 100, horizontalDistance);
        }

        // Draw right
        if (node.right != null) {
            canvas.drawLine(x, y, x + horizontalDistance, y + 100, paint);
            drawTree(canvas, node.right, x + horizontalDistance, y + 100, horizontalDistance);
        }

        // Draw the node
        canvas.drawCircle(x, y, 40, paint);
        canvas.drawText(String.valueOf(node.value), x - 15, y + 10, paint);


        // Draw the node circle
        paint.setColor(Color.BLACK); // Node circle color
        canvas.drawCircle(x, y, 40, paint);


        // Draw the node value
        paint.setColor(Color.WHITE); // Text color
        paint.setTextAlign(Paint.Align.CENTER); // Align text in the middle
        float textY = y - ((paint.descent() + paint.ascent()) / 2); // Adjust text position
        canvas.drawText(String.valueOf(node.value), x, textY, paint);
    }


}
