package au.edu.anu.comp6442.group03.studyapp.treevisualizer;

import java.util.HashMap;

public class NodeAndCoordinates {
    private final HashMap<Integer, LevelAndCoordinates> TreeCoord;

    public NodeAndCoordinates() {
        this.TreeCoord = new HashMap<Integer, LevelAndCoordinates>();
    }
    public void updateNodeCoordinate(int nodeValue, LevelAndCoordinates levelCoord){
        this.TreeCoord.put(nodeValue, levelCoord);
    }

    public HashMap<Integer, LevelAndCoordinates> getNodeMap(){
        return this.TreeCoord;
    }

    public CoordPair getCoordinate(int nodeValue){
        return this.TreeCoord.get(nodeValue).getCoordinatePair();
    }

    public int getLevel(int nodeValue){
        return this.TreeCoord.get(nodeValue).getLevel();
    }

    public void emptyNodeCoordinate(){
        this.TreeCoord.clear();
    }


}
