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
package cn.crowdos.kernel.constraint.PrivacyConstraint;

import cn.crowdos.kernel.constraint.Condition;

import java.math.BigInteger;

public class EncryptCoordinate implements Condition {
    final Integer encryptedlongitude;
    final Integer encryptedlatitude;

    /**
     * The EncryptCoordinate function takes in a longitude and latitude coordinate,
     * encrypts them using the encryption algorithm, and returns an EncryptCoordinate object.

     *
     * @param encryptedlongitude Set the encryptedlongitude variable
     * @param encryptedlatitude Set the encryptedlatitude variable
     *
     */
    public EncryptCoordinate(Integer encryptedlongitude, Integer encryptedlatitude) {
        this.encryptedlongitude = encryptedlongitude;
        this.encryptedlatitude = encryptedlatitude;
    }

    public EncryptCoordinate(double longitude,double latitude){
        Paillier paillier = new Paillier();
        this.encryptedlongitude = Integer.valueOf(String.valueOf(paillier.encrypt(longitude)));
        this.encryptedlatitude = Integer.valueOf(String.valueOf(paillier.encrypt(latitude)));
    }

    @Override
    /**
     * The hashCode function is used to generate a unique hash code for each object.
     * This function is used by the Java HashMap class, which allows us to store objects in a map.
     * The hashCode function must be overridden when we override the equals method, as it relies on 
     * the hashCode of an object being equal if and only if two objects are equal (as defined by our 
     * own equals method). In this case, we simply add together the encrypted latitude and longitude values.
     *
     * @return The sum of the encrypted latitude and longitude
     */
    public int hashCode() {
        return this.encryptedlatitude+this.encryptedlongitude;
    }

    /**
     * The equals function is used to compare two EncryptCoordinate objects.
     *
     * @param obj Compare the current object with another object
     *
     * @return True if the two encryptcoordinate objects are equal
     */
    @Override
    public boolean equals(Object obj){
        if(this == obj) return true;
        if (obj instanceof EncryptCoordinate){
            EncryptCoordinate anEnCoord = (EncryptCoordinate) obj;
            Paillier paillier = new Paillier();
            BigInteger result1 = paillier.add(BigInteger.valueOf(this.encryptedlongitude),BigInteger.valueOf(this.encryptedlatitude));
            BigInteger result2 = paillier.add(BigInteger.valueOf(anEnCoord.encryptedlongitude),BigInteger.valueOf(anEnCoord.encryptedlatitude));
            return result1.compareTo(result2)==0;
        }
        return false;
    }
}
