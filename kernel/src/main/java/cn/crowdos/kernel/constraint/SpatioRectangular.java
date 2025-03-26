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

import cn.crowdos.kernel.Decomposer;

import java.util.Collections;
import java.util.List;

public class SpatioRectangular implements Constraint {

    private final Coordinate[] range;

    public SpatioRectangular(Coordinate topLeft, Coordinate bottomRight) throws InvalidConstraintException {
        if (topLeft.inLine(bottomRight)) throw new InvalidConstraintException();
        this.range = new Coordinate[]{topLeft, bottomRight};
    }

    public SpatioRectangular(Coordinate bottomRight) throws InvalidConstraintException {
        Coordinate topLeft = new Coordinate();
        if (topLeft.inLine(bottomRight)) throw new InvalidConstraintException();
        this.range = new Coordinate[]{topLeft, bottomRight};
    }

    @Override
    public boolean satisfy(Condition condition) {
        if (!(condition instanceof Coordinate)) return false;
        Coordinate coord = (Coordinate) condition;
        return range[0].longitude <= coord.longitude && range[0].latitude <= coord.latitude
                && coord.longitude < range[1].longitude && coord.latitude < range[1].latitude;
    }

    @Override
    public Class<? extends Condition> getConditionClass() {
        return Coordinate.class;
    }

    @Override
    public String description() {
        return toString();
    }

    @Override
    public Decomposer<Constraint> decomposer() {
        return new Decomposer<Constraint>() {
            @Override
            public List<Constraint> trivialDecompose() {
                try {
                    return Collections.singletonList(new SpatioRectangular(range[0], range[1]));
                } catch (InvalidConstraintException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public List<Constraint> scaleDecompose(int scale) {
                //waring
                return this.trivialDecompose();
            }
        };
    }

    @Override
    public String toString() {
        return "SpatioRectangular(" + range[0] + "," + range[1] + ')';
    }
}
