/*
 * Copyright 2019-2025 CrowdOS Group, Northwestern Polytechnical University. <https://github.com/crowdosNWPU/CrowdOS>
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

import java.util.Collections;
import java.util.List;

public class POIConstraint implements Constraint{
    private final Coordinate location;
    private final double satisfyRadius;

    public POIConstraint(Coordinate location) {
        this(location, 10);
    }

    public POIConstraint(Coordinate location, double satisfyRadius){
        this.location = location;
        this.satisfyRadius = satisfyRadius;
    }

    public Coordinate getLocation() {
        return location;
    }


    @Override
    public Decomposer<Constraint> decomposer() {
        return new Decomposer<Constraint>() {
            @Override
            public List<Constraint> trivialDecompose() {
                return Collections.singletonList(new POIConstraint(location));
            }

            @Override
            public List<Constraint> scaleDecompose(int scale) throws DecomposeException {
                throw new DecomposeException("unsupported");
            }
        };
    }


    @Override
    public boolean satisfy(Condition condition) {
        if (! ( condition instanceof Coordinate)) return false;
        Coordinate participantLocation = (Coordinate) condition;
        return participantLocation.euclideanDistance(location) <= satisfyRadius * satisfyRadius;
    }


    @Override
    public Class<? extends Condition> getConditionClass() {
        return Coordinate.class;
    }


    @Override
    public String description() {
        return "POI Constraint";
    }
}
