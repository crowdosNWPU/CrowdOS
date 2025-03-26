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
            if(width<0)
                throw new InvalidConstraintException();
    }

    /**
     *
     * Constructs a DecomposeSpatioLine object with a given width and an array of coordinates.
     * @param width the width of the SpatioLines
     * @param pointsList an array of coordinates to be decomposed into SpatioLines
     * @throws InvalidConstraintException if an invalid constraint is detected
     */
    public DecomposeSpatioLine(double width,Coordinate...pointsList) throws InvalidConstraintException{
        this(width,Arrays.asList(pointsList));
        if(width<0)
            throw new InvalidConstraintException();
    }

    /**
     *
     * Decomposes the list of coordinates into a list of SpatioLines.
     * @return a list of SpatioLines that represent the decomposed coordinates
     * @throws InvalidConstraintException if an invalid constraint is detected
     */
    public List<SpatioLine> DecomposePointslistToLines() throws InvalidConstraintException {
        if(width<0)
            throw new InvalidConstraintException();
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
