/*
 * Copyright 2019-2025 CrowdOS_Group, Inc. <https://github.com/crowdosNWPU/CrowdOS>
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * A SpatioPolygon class represents a polygon in a two-dimensional space. It implements the Constraint interface.
 */

public class SpatioPolygon implements Constraint{
    private final List<Coordinate> polygon;

    /**
     * Constructs a SpatioPolygon with the given list of coordinates.
     *
     * @param polygon the list of coordinates representing the polygon vertices.
     * @throws InvalidConstraintException if the polygon is invalid.
     */

    public SpatioPolygon(List<Coordinate> polygon) throws InvalidConstraintException{
        this.polygon = polygon;
        if(!PolygonValidityChecker(polygon)){
            throw new InvalidConstraintException(String.format("polygon is invalid"));
        }
    }

    /**
     * Constructs a SpatioPolygon with the given array of coordinates.
     *
     * @param polygon the array of coordinates representing the polygon vertices.
     * @throws InvalidConstraintException if the polygon is invalid.
     */
    public SpatioPolygon(Coordinate... polygon) throws InvalidConstraintException {
        this(Arrays.asList(polygon));
    }

    /**
     * Returns a Decomposer object that decomposes the constraint.
     *
     * @return a Decomposer object that decomposes the constraint.
     */
    @Override
    public Decomposer<Constraint> decomposer() {
        return new Decomposer<Constraint>() {
            @Override
            public List<Constraint> trivialDecompose() {
                try {
                    return Collections.singletonList(new SpatioPolygon(polygon));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public List<Constraint> scaleDecompose(int scale) throws DecomposeException {
                if(scale<0) throw new DecomposeException("invalid decompose scale");
                if(scale==1) return trivialDecompose();
                List<Constraint> subConstraints = new ArrayList<>();
                try {
                     subConstraints = ComputeSubPolygon(polygon,(int) Math.sqrt(scale));
                } catch (InvalidConstraintException e) {
                    throw new RuntimeException(e);
                }
                return subConstraints;
            }
        };
    }

    /**
     * Checks if the given condition is satisfied by the constraint.
     *
     * @param condition the condition to check.
     * @return true if the condition is satisfied by the constraint, false otherwise.
     */

    @Override
    public boolean satisfy(Condition condition) {
        if(!(condition instanceof Coordinate)) return false;
        Coordinate coordinate = (Coordinate) condition;
        return PointInPolygon(coordinate,polygon);
    }

    /**
     * Returns the class of the condition of the constraint.
     *
     * @return the class of the condition of the constraint.
     */
    @Override
    public Class<? extends Condition> getConditionClass() {
        return Coordinate.class;
    }

    /**
     * Returns a string representation of the constraint.
     *
     * @return a string representation of the constraint.
     */
    @Override
    public String toString(){
        String str = "SpatioPolygon{";
        for(Object item:polygon){
            str += item;
            str += ";";
        }
        str += "}";
        return str;
    }

    /**
     * Returns a description of the constraint.
     *
     * @return a description of the constraint.
     */
    @Override
    public String description() {
        return toString();
    }

    /**
     *
     * Checks the validity of a polygon represented as a list of coordinates.
     *
     * @param polygon a list of coordinates representing the polygon
     * @return true if the polygon is valid, false otherwise
     */

    private boolean PolygonValidityChecker (List<Coordinate> polygon){
        int pointsNum = polygon.size();
        if(pointsNum<3) return false;

        for(int i=0;i<polygon.size();i++){
            Coordinate p1 = polygon.get(i);
            Coordinate p2 = polygon.get((i+1)%pointsNum);
            if(p1.equals(p2)) return false;
        }

        for (int i = 0; i < pointsNum; i++) {
            Coordinate A = polygon.get(i);
            Coordinate B = polygon.get((i + 1) % pointsNum);
            for (int j = i + 2; j < pointsNum; j++) {
                Coordinate C = polygon.get(j);
                Coordinate D = polygon.get((j + 1) % pointsNum);
                if (SegmentsIntersect(A, B, C, D)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     *
     * Checks if two line segments intersect.
     * @param A the first endpoint of the first line segment
     * @param B the second endpoint of the first line segment
     * @param C the first endpoint of the second line segment
     * @param D the second endpoint of the second line segment
     * @return true if the line segments intersect, false otherwise
     */
    private boolean SegmentsIntersect(Coordinate A, Coordinate B, Coordinate C, Coordinate D) {
        double ccw1 = ccw(A, B, C);
        double ccw2 = ccw(A, B, D);
        double ccw3 = ccw(C, D, A);
        double ccw4 = ccw(C, D, B);
        return (ccw1 * ccw2 < 0) && (ccw3 * ccw4 < 0);
    }


    /**
     *
     * Calculates the cross product of two vectors.
     * @param A the first vector
     * @param B the second vector
     * @param C the third vector
     * @return the cross product of AB and AC
     */
    private double ccw(Coordinate A, Coordinate B, Coordinate C) {
        return (B.longitude - A.longitude) * (C.latitude - A.latitude) - (B.latitude - A.latitude) * (C.longitude - A.longitude);
    }


    /**
     *
     * Determines if a given point is inside a polygon by counting the number of times a line from the point to the right intersects with the polygon.
     * @param point The point to check for inside the polygon.
     * @param polygon The list of vertices that form the polygon.
     * @return true if the point is inside the polygon, false otherwise.
     */
    public static boolean PointInPolygon(Coordinate point, List<Coordinate> polygon) {
        int crossings = 0;
        for (int i = 0; i < polygon.size(); i++) {
            Coordinate vertex1 = polygon.get(i);
            Coordinate vertex2 = polygon.get((i + 1) % polygon.size());

            if (vertex1.longitude== vertex2.longitude) {
                continue;
            }

            if (point.longitude < Math.min(vertex1.longitude, vertex2.longitude)) {
                continue;
            }

            if (point.longitude >= Math.max(vertex1.longitude, vertex2.longitude)) {
                continue;
            }

            double slope = (vertex2.latitude - vertex1.latitude) / (vertex2.longitude - vertex1.longitude);
            double intersect = vertex1.latitude + (point.longitude - vertex1.longitude) * slope;

            if (intersect > point.latitude) {
                crossings++;
            }
        }

        return (crossings % 2 != 0);
    }

    /**
     *
     * Divides a polygon into sub-polygons by dividing its bounding box into a grid of smaller rectangles and testing if each rectangle is inside the polygon.
     *
     * @param polygon The polygon to divide into sub-polygons.
     * @param copies The number of times to divide the bounding box in each direction.
     * @return A list of sub-polygons that cover the area of the original polygon.
     * @throws InvalidConstraintException if a sub-polygon could not be constructed from the given coordinates.
     */
    public List<Constraint> ComputeSubPolygon(List<Coordinate> polygon, int copies) throws InvalidConstraintException {
        List<Constraint>subPolygons = new ArrayList<>();

        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE;
        for (Coordinate point : polygon) {
            if (point.longitude < minX) minX = point.longitude;
            if (point.longitude > maxX) maxX = point.longitude;
            if (point.latitude < minY) minY = point.latitude;
            if (point.latitude > maxY) maxY = point.latitude;
        }

        double xstep = (maxX - minX)/copies;
        double ystep = (maxY - minY)/copies;


        Coordinate topLeft = new Coordinate(minX, maxY);
        Coordinate topRight = new Coordinate(maxX, maxY);
        Coordinate bottomLeft = new Coordinate(minX, minY);
        Coordinate bottomRight = new Coordinate(maxX, minY);

        List<Coordinate[]> cells = new ArrayList<>();
        for (double x = minX; x < maxX; x += xstep) {
            for (double y = minY; y < maxY; y += ystep) {
                Coordinate cellTopLeft = new Coordinate(x, y);
                Coordinate cellBottomRight = new Coordinate(x + xstep, y - ystep);
                cells.add(new Coordinate[]{cellTopLeft, cellBottomRight});
            }
        }

        List<Coordinate> result = new ArrayList<>();
        for (Coordinate[] cell : cells) {
            Coordinate cellCenter = new Coordinate(cell[0].longitude + xstep / 2, cell[1].latitude + ystep / 2);
            if (PointInPolygon(cellCenter,polygon)) {
                result.add(cell[0]);
                result.add(new Coordinate(cell[0].longitude, cell[1].latitude));
                result.add(cell[1]);
                result.add(new Coordinate(cell[1].longitude, cell[0].latitude));
            }
        }
        for(int i=0;i<result.size();i++){
            Coordinate firstPoint = result.get(i);
            Coordinate secondPoint = new Coordinate(firstPoint.longitude,firstPoint.latitude+ystep);
            Coordinate thirdPoint = new Coordinate(firstPoint.longitude+xstep,firstPoint.latitude+ystep);
            SpatioPolygon subPolygon = new SpatioPolygon(firstPoint,secondPoint,thirdPoint);
            subPolygons.add(subPolygon);
        }
        return subPolygons;
    }
}

