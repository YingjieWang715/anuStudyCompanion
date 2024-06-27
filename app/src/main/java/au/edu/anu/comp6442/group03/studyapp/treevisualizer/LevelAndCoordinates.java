package au.edu.anu.comp6442.group03.studyapp.treevisualizer;

public class LevelAndCoordinates {
    private final int level;
    private final CoordPair coordinate;

    public LevelAndCoordinates(int level, CoordPair coordinate){
        this.level = level;
        this.coordinate = coordinate;
    }

    public int getLevel(){
        return this.level;
    }

    public CoordPair getCoordinatePair(){
        return this.coordinate;
    }
}
