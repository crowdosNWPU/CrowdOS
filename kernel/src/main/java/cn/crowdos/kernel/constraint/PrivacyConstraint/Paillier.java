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

import java.math.BigInteger;
import java.security.SecureRandom;

public class Paillier {
    private BigInteger p, q, lambda;
    public BigInteger n, nsquare, g;
    private SecureRandom random;

    /**
     * The Paillier function takes in two BigIntegers and returns a new BigInteger
     * that is the result of encrypting the first parameter with the second.
     *
     */
    public Paillier() {
        random = new SecureRandom();
        p = new BigInteger(1024, 64, random);
        q = new BigInteger(1024, 64, random);
        n = p.multiply(q);
        nsquare = n.multiply(n);
        g = new BigInteger("2");
        lambda = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE))
                .divide(p.subtract(BigInteger.ONE).gcd(q.subtract(BigInteger.ONE)));
    }

    /**    
     * The encrypt function takes a double coordinate and returns an encrypted BigInteger.
     *
     * @param coordinate Convert the coordinate into a biginteger
     * @return A biginteger
     */
    public BigInteger encrypt(double coordinate) {
        // 转换为整数类型
        BigInteger m = BigInteger.valueOf((long) (coordinate * 1000000));
        BigInteger r = new BigInteger(512, random);
        BigInteger c = g.modPow(m, nsquare).multiply(r.modPow(n, nsquare)).mod(nsquare);
        return c;
    }

    public BigInteger multiply(BigInteger a, int k) {
        return a.modPow(BigInteger.valueOf(k), nsquare);
    }

    /**
     * Returns the product of two BigIntegers modulo nsquare.
     *
     * @param a the first BigInteger to be added.
     * @param b the second BigInteger to be added.
     * @return the product of a and b, modulo nsquare.
     */
    public BigInteger add(BigInteger a, BigInteger b) {
        return a.multiply(b).mod(nsquare);
    }

    /**
     * Decrypts the given ciphertext using the Paillier cryptosystem.
     *
     * @param c the ciphertext to decrypt, represented as a BigInteger.
     * @return the decrypted plaintext, represented as a BigInteger.
     */
    public BigInteger decrypt(BigInteger c) {
        BigInteger u = g.modPow(lambda, nsquare).subtract(BigInteger.ONE).divide(n).modInverse(n);
        BigInteger m = c.modPow(lambda, nsquare).subtract(BigInteger.ONE).divide(n).multiply(u).mod(n);
        return m;
    }
}
