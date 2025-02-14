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
package cn.crowdos.kernel.constraint.PrivacyConstraint;

import cn.crowdos.kernel.Decomposer;
import cn.crowdos.kernel.constraint.*;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

public class SimpleEncryptSpatioConstraint implements Constraint {
    private final EncryptCoordinate[] range;

    /**
     *
     * Constructs a new SimpleEncryptSpatioConstraint object with the given top-left and bottom-right coordinates.
     * @param topLeft the EncryptCoordinate object representing the top-left corner of the range
     * @param bottomRight the EncryptCoordinate object representing the bottom-right corner of the range
     * @throws InvalidConstraintException if the given coordinates are not valid for creating a spatio constraint
     */
    public SimpleEncryptSpatioConstraint(EncryptCoordinate topLeft, EncryptCoordinate bottomRight) throws InvalidConstraintException {
        this.range = new EncryptCoordinate[]{topLeft,bottomRight};
    }

    /**
     *
     * This method checks whether a given condition is satisfied or not based on the encrypted coordinates.
     * It returns true if the given condition is an instance of EncryptCoordinate, else it returns false.
     * @param condition a condition object which needs to be checked
     * @return true if the given condition is satisfied, false otherwise
     */

    @Override
    public boolean satisfy(Condition condition) {
        if(!(condition instanceof EncryptCoordinate)) return false;
        EncryptCoordinate encryptCoordinate = (EncryptCoordinate) condition;
        Paillier paillier = new Paillier();
        BigInteger resTL = paillier.add(BigInteger.valueOf(range[0].encryptedlongitude),BigInteger.valueOf(range[0].encryptedlatitude));
        BigInteger resBR = paillier.add(BigInteger.valueOf(range[1].encryptedlongitude),BigInteger.valueOf(range[1].encryptedlatitude));
        BigInteger resParticipent = paillier.add(BigInteger.valueOf(encryptCoordinate.encryptedlongitude),BigInteger.valueOf(encryptCoordinate.encryptedlatitude));
        return resParticipent.compareTo(resTL) >0 && resParticipent.compareTo(resBR)<0;
    }

    /**
     *
     * Returns a Decomposer object that can decompose constraints.
     *
     * @return {@code Decomposer<Constraint>} object for decomposing constraints
     */

    @Override
    public Decomposer<Constraint> decomposer() {
        return new Decomposer<Constraint>() {
            /**
             * Returns a list of trivial constraints for SimpleEncryptSpatioConstraint.
             *
             * @return List of SimpleEncryptSpatioConstraint
             * @throws RuntimeException if InvalidConstraintException is thrown
             */
            @Override
            public List<Constraint> trivialDecompose() {
                try {
                    return Collections.singletonList(new SimpleEncryptSpatioConstraint(range[0], range[1]));
                } catch (InvalidConstraintException e) {
                    throw new RuntimeException(e);
                }
            }

            /**
             * Returns a list of trivial constraints for SimpleEncryptSpatioConstraint.
             *
             * @param scale The scale to use for scaling the constraints
             * @return List of SimpleEncryptSpatioConstraint
             */

            @Override
            public List<Constraint> scaleDecompose(int scale) {
                //waring
                return this.trivialDecompose();
            }
        };
    }


    /**
     *
     * Returns the class of Condition used in this class.
     * @return Class object for Coordinate
     */
    @Override
    public Class<? extends Condition> getConditionClass() {
        return Coordinate.class;
    }

    /**
     *
     * Returns a String representation of the SimpleEncryptSpatioConstraint object.
     * @return String representation of the object
     */
    @Override
    public String toString() {
        return "SimpleEncryptSpatioConstraint("+range[0]+","+range[1]+')';
    }

    /**
     *
     * Returns a description of the SimpleEncryptSpatioConstraint object.
     *  @return Description of the object as a String
     */

    @Override
    public String description() {
        return toString();
    }
}
