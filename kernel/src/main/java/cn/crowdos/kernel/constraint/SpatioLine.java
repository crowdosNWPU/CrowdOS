/*
 * Copyright 2019-2025 CrowdOS Group, Northwestern Polytechnical University, Inc. <https://github.com/crowdosNWPU/CrowdOS>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * imitations under the License.
 */
package cn.crowdos.kernel.constraint;

import cn.crowdos.kernel.DecomposeException;
import cn.crowdos.kernel.Decomposer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 *
 * The SpatioLine class represents a line in a 2D coordinate system with a given width.
 * It implements the Constraint interface and provides methods for decomposition and satisfaction testing.
 */
public class SpatioLine implements Constraint{
    private final Coordinate startPoint;
    private final Coordinate endPoint;
    private final double width;

    /**
     * Constructs a new SpatioLine with the specified start and end points and width.
     *
     * @param startPoint the start point of the SpatioLine
     * @param endPoint the end point of the SpatioLine
     * @param width the width of the SpatioLine
     * @throws InvalidConstraintException if the width is less than or equal to zero
     */

    public SpatioLine(Coordinate startPoint, Coordinate endPoint, double width) throws InvalidConstraintException{
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.width = width;
        if(width<=0){
            throw new InvalidConstraintException(String.format("width %s is invalid",this.width));
        }
    }
    /**
     * Returns a Decomposer object that can be used to decompose this constraint into sub-constraints.
     * This method returns a new anonymous Decomposer object that implements the trivialDecompose() and scaleDecompose() methods.
     * @return A Decomposer object that can be used to decompose this constraint.
     */
    @Override
    public Decomposer<Constraint> decomposer() {
        return new Decomposer<Constraint>() {
            /**
             * Returns a list containing a single SpatioLine object representing the original constraint.
             * @return A list containing a single SpatioLine object representing the original constraint.
             */
            @Override
            public List<Constraint> trivialDecompose() {
                try{
                    return Collections.singletonList(new SpatioLine(startPoint,endPoint,width));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            /**
             * Scales the original constraint by the given scale factor and returns a list of sub-constraints.
             * The sub-constraints are SpatioLine objects that represent equally spaced intervals along the original line segment.
             * The total number of sub-constraints is equal to the scale factor.
             * @param scale The scale factor used to decompose the constraint.
             * @return A list of sub-constraints.
             * @throws DecomposeException If the scale factor is less than zero.
             */

            @Override
            public List<Constraint> scaleDecompose(int scale) throws DecomposeException {
                if(scale<0) throw new DecomposeException("invalid decompose scale");
                if(scale==1) return trivialDecompose();
                double totalDistance = ComputePointToPointDistance(startPoint,endPoint);
                double distancePerIntervel = totalDistance/scale;
                List<Constraint> subConstraints = new ArrayList<>(scale);
                for(int i=0;i<scale;i++){
                    Coordinate intervelStartPoint = ComputePoint(i*distancePerIntervel);
                    Coordinate intervelEndPoint = ComputePoint((i+1)*distancePerIntervel);
                    try{
                        subConstraints.add(new SpatioLine(intervelStartPoint,intervelEndPoint,width));

                    } catch (InvalidConstraintException e) {
                        throw new RuntimeException(e);
                    }
                }
                return subConstraints;
            }
        };
    }


    /**
     *
     * Checks if a given Coordinate satisfies the condition of being within a certain distance of the spatioline.
     * @param condition the Coordinate to check
     * @return true if the Coordinate is within the distance, false otherwise
     */

    @Override
    public boolean satisfy(Condition condition) {
        if(!(condition instanceof Coordinate)) return false;
        Coordinate coordinate = (Coordinate) condition;
        return ComputePointToLineDistance(startPoint,endPoint,coordinate)<=width/2;
    }

    /**
     *
     * Gets the class of the condition that this SpatioLine object accepts.
     * @return the class of the accepted condition
     */

    @Override
    public Class<? extends Condition> getConditionClass() {
        return Coordinate.class;
    }

    /**
     *
     * Returns a String representation of this SpatioLine object.
     * @return a String representation of the object
     */
    @Override
    public String toString(){
        return "SpatioLine{" +
                "startPoint=" + startPoint +
                "endPoint=" + endPoint +
                "width=" + width +
                "}";
    }

    /**
     *
     * Returns a description of this SpatioLine object.
     * @return a description of the object
     */

    @Override
    public String description() {
        return toString();
    }

    /**
     *
     * Computes the distance between a point and the line segment defined by the starting point and ending point.
     * @param start the starting point of the line segment
     * @param end the ending point of the line segment
     * @param point the point to compute the distance to
     * @return the distance between the point and the line segment
     */
    private double ComputePointToLineDistance(Coordinate start,Coordinate end,Coordinate point){
        double x = point.longitude, y = point.latitude;
        double x1 = start.longitude, y1 = start.latitude;
        double x2 = end.longitude, y2 = end.latitude;
        double cross = (x2 - x1) * (x - x1) + (y2 - y1) * (y - y1);
        if (cross <= 0) {
            return Math.sqrt((x - x1) * (x - x1) + (y - y1) * (y - y1));
        }
        double d2 = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
        if (cross >= d2) {
            return Math.sqrt((x - x2) * (x - x2) + (y - y2) * (y - y2));
        }
        double r = cross / d2;
        double px = x1 + (x2 - x1) * r;
        double py = y1 + (y2 - y1) * r;
        return Math.sqrt((x - px) * (x - px) + (y - py) * (y - py));
    }

    /**
     *
     * This method computes the Euclidean distance between two points on a 2D plane, given their coordinates.
     * The distance is computed as the square root of the sum of the squares of the differences between the longitudes and latitudes.
     * @param point1 The first point's coordinates, given as a Coordinate object.
     * @param point2 The second point's coordinates, given as a Coordinate object.
     * @return The Euclidean distance between the two points, as a double.
     */

    private double ComputePointToPointDistance(Coordinate point1,Coordinate point2){
        double longitudeDifference = point1.longitude - point2.longitude;
        double latitudeDifference = point1.latitude - point2.latitude;
        return Math.sqrt(longitudeDifference*longitudeDifference+latitudeDifference*latitudeDifference);
    }

    /**
     *
     * This method computes the coordinates of a point at a certain distance along a straight line between two other points.
     * The line is defined by the start point and end point, given as Coordinate objects.
     * The distance is given as a double, and represents the distance from the start point to the computed point.
     * @param distance The distance from the start point to the computed point, given as a double.
     * @return The coordinates of the computed point, given as a Coordinate object.
     */

    private Coordinate ComputePoint(double distance){
        if(endPoint.latitude==startPoint.latitude){
            if(endPoint.longitude>startPoint.longitude) return new Coordinate(startPoint.longitude+distance,startPoint.latitude);
            else return new Coordinate(startPoint.longitude-distance,startPoint.latitude);
        }
        else if(endPoint.longitude == startPoint.longitude){
            if(endPoint.latitude>startPoint.latitude) return new Coordinate(startPoint.longitude,startPoint.latitude+distance);
            else return new Coordinate(startPoint.longitude, startPoint.latitude-distance);
        }
        else{
            double k = (endPoint.latitude-startPoint.latitude)/(endPoint.longitude-startPoint.longitude);
            double newLongitude = startPoint.longitude + (distance/Math.sqrt(1+k*k));
            double newLatitude = startPoint.latitude + k*(newLongitude-startPoint.longitude);
            return new Coordinate(newLongitude,newLatitude);
         }
    }
}
