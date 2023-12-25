package cn.crowdos.kernel.constraint;

import cn.crowdos.kernel.DecomposeException;
import cn.crowdos.kernel.Decomposer;

import java.util.Collections;
import java.util.List;

/**
 *
 * A spatial constraint that defines a circular region around a center point with a given radius.
 * This class implements the Constraint interface and checks if a given Coordinate satisfies the constraint.
 * It also provides a Decomposer for the SpatioPoint constraint.
 */
public class SpatioPoint implements Constraint{
    private final Coordinate center;
    private final double radius;

    /**
     *
     * Constructs a new SpatioPoint constraint with the given center point and radius.
     * @param center The center point of the circular region.
     * @param radius The radius of the circular region.
     * @throws InvalidConstraintException if the radius is less than or equal to 0.
     */
    public SpatioPoint(Coordinate center, double radius) throws InvalidConstraintException{
        this.center = center;
        this.radius = radius;
        if(radius<=0){
            throw new InvalidConstraintException(String.format("radius %s is invalid",this.radius));
        }
    }

    /**
     *
     * Checks if the given Condition satisfies the SpatioPoint constraint.
     * @param condition The Condition to check.
     * @return true if the Condition is a Coordinate within the circular region defined by the SpatioPoint constraint, false otherwise.
     */

    @Override
    public boolean satisfy(Condition condition) {
        if(!(condition instanceof Coordinate)) return false;
        Coordinate coordinate = (Coordinate) condition;
        return ComputePointToPointDistance(coordinate,center)<=radius;
    }

    /**
     *
     * Provides a Decomposer for the SpatioPoint constraint that returns a List containing only the SpatioPoint constraint itself.
     *
     * @return a new Decomposer instance.
     */
    @Override
    public Decomposer<Constraint> decomposer() {
        return new Decomposer<Constraint>() {
            @Override
            public List<Constraint> trivialDecompose() {
                try{
                    return Collections.singletonList(new SpatioPoint(center,radius));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public List<Constraint> scaleDecompose(int scale) throws DecomposeException {
                return this.trivialDecompose();
            }
        };
    }

    /**
     *
     * Gets the Condition class that this Constraint applies to.
     * @return the Coordinate class.
     */
    @Override
    public Class<? extends Condition> getConditionClass() {
        return Coordinate.class;
    }

    /**
     *
     * Gets a string representation of the SpatioPoint constraint.
     * @return a string representing the SpatioPoint constraint.
     */
    @Override
    public String toString(){
        return "SpatioPoint{" +
                "Center=" + center +
                "radius=" + radius +
                "}";
    }

    /**
     *
     * Gets a description of the SpatioPoint constraint.
     * @return a string representing the SpatioPoint constraint.
     */
    @Override
    public String description() {
        return toString();
    }

    /**
     *
     * Computes the Euclidean distance between two Coordinate points.
     * @param point1 The first Coordinate.
     * @param point2 The second Coordinate.
     * @return the Euclidean distance between the two points.
     */
    private double ComputePointToPointDistance(Coordinate point1,Coordinate point2){
        double longitudeDifference = point1.longitude - point2.longitude;
        double latitudeDifference = point1.latitude - point2.latitude;
        return Math.sqrt(longitudeDifference*longitudeDifference+latitudeDifference*latitudeDifference);
    }
}
