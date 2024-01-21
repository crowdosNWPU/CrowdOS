package cn.crowdos.kernel.constraint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 *
 * The DecomposeSpatioLine class is responsible for decomposing a set of coordinates into a list of SpatioLines.
 * A SpatioLine represents a line in a 2D space with a given width.
 */

public class DecomposeSpatioLine {
    private final List<Coordinate>pointsList;
    private final double width;

    /**
     *
     * Constructs a DecomposeSpatioLine object with a given width and a list of coordinates.
     * @param width the width of the SpatioLines
     * @param pointsList a list of coordinates to be decomposed into SpatioLines
     * @throws InvalidConstraintException if an invalid constraint is detected
     */
    public DecomposeSpatioLine(double width,List<Coordinate> pointsList) throws InvalidConstraintException {
        this.pointsList = pointsList;
        this.width = width;
         if(pointsList==null)
            throw new InvalidConstraintException("Invalid pointsList");
         
    }

    /**
     *
     * Constructs a DecomposeSpatioLine object with a given width and an array of coordinates.
     * @param width the width of the SpatioLines
     * @param pointsList an array of coordinates to be decomposed into SpatioLines
     * @throws InvalidConstraintException if an invalid constraint is detected
     */
    public DecomposeSpatioLine(double width,Coordinate...pointsList) throws InvalidConstraintException {
        this(width,Arrays.asList(pointsList));
        if(pointsList==null)
            throw new InvalidConstraintException("Invalid pointsList");
    }

    /**
     *
     * Decomposes the list of coordinates into a list of SpatioLines.
     * @return a list of SpatioLines that represent the decomposed coordinates
     * @throws InvalidConstraintException if an invalid constraint is detected
     */
    public List<SpatioLine> DecomposePointslistToLines() throws InvalidConstraintException {
        if(pointsList==null)
            throw new InvalidConstraintException("Invalid pointsList");
        List<SpatioLine> decomposeLines = new ArrayList<>(pointsList.size()-1);
        for(int i=0;i<pointsList.size()-1;i++){
            Coordinate startPoint = pointsList.get(i);
            Coordinate endPoint = pointsList.get(i+1);
            SpatioLine spatioLine = new SpatioLine(startPoint,endPoint,width);
            decomposeLines.add(spatioLine);
        }
        return decomposeLines;
    }
}
