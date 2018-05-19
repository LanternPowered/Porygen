/*
 * This file is part of Porygen, licensed under the MIT License (MIT).
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the Software), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED AS IS, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.lanternpowered.porygen.util.dsi;

// Note: This file contains custom modifications for Porygen licensed under
// the MIT License.

// From the DSI utilities project

// @formatter:off

/*
 * DSI utilities
 *
 * Copyright (C) 2013-2015 Sebastiano Vigna
 *
 *  This library is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU Lesser General Public License as published by the Free
 *  Software Foundation; either version 3 of the License, or (at your option)
 *  any later version.
 *
 *  This library is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 *  for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, see <http://www.gnu.org/licenses/>.
 *
 */

import java.util.Random;

/** A fast, high-quality {@linkplain Random pseudorandom number generator} that
 * returns the sum of consecutive outputs of a handcrafted linear generator with 128 bits of state.
 * under every respect: it is faster and has stronger statistical properties.
 * More details can be found on the <a href="http://xorshift.di.unimi.it/"><code>xoroshiro+</code>/<code>xorshift*</code>/<code>xorshift+</code>
 * generators and the PRNG shootout</a> page.
 *
 * <p><strong>Warning</strong>: the output of this generator might change in the near future.
 *
 * <p>Note that this is
 * <strong>not</strong> a cryptographic-strength pseudorandom number generator, but its quality is
 * preposterously higher than {@link Random}'s, and its cycle length is
 * 2<sup>128</sup>&nbsp;&minus;&nbsp;1, which is more than enough for any single-thread application.
 *
 * <p>By using the supplied {@link #jump()} method it is possible to generate non-overlapping long sequences
 * for parallel computations. This class provides also a {@link #split()} method to support recursive parallel computations, in the spirit of
 * Java 8's <a href="http://docs.oracle.com/javase/8/docs/api/java/util/SplittableRandom.html"><code>SplittableRandom</code></a>.
 */
@SuppressWarnings("javadoc")
public class XoRoShiRo128PlusRandom extends Random {
    private static final long serialVersionUID = 1L;
    /** The internal state of the algorithm. */
    private long s0, s1;

    /** Creates a new generator seeded using {@link SeedUtil#randomSeed()}. */
    public XoRoShiRo128PlusRandom() {
        this(SeedUtil.randomSeed());
    }

    /** Creates a new generator using a given seed.
     *
     * @param seed a seed for the generator.
     */
    public XoRoShiRo128PlusRandom(final long seed) {
        setSeed(seed);
    }

    /** An internal constructor that is used by the {@link #split()} method. */
    private XoRoShiRo128PlusRandom(final XoRoShiRo128PlusRandom randomToSplit) {
        s0 = HashCommon.murmurHash3(randomToSplit.s0);
        s1 = HashCommon.murmurHash3(randomToSplit.s1);
    }

    @Override
    public long nextLong() {
        final long s0 = this.s0;
        long s1 = this.s1;
        final long result = s0 + s1;
        s1 ^= s0;
        this.s0 = Long.rotateLeft(s0, 55) ^ s1 ^ s1 << 14;
        this.s1 = Long.rotateLeft(s1, 36);
        return result;
    }

    @Override
    public int nextInt() {
        return (int)(nextLong() >>> 32);
    }

    @Override
    public int nextInt(final int n) {
        return (int)nextLong(n);
    }

    /** Returns a pseudorandom uniformly distributed {@code long} value
     * between 0 (inclusive) and the specified value (exclusive), drawn from
     * this random number generator's sequence. The algorithm used to generate
     * the value guarantees that the result is uniform, provided that the
     * sequence of 64-bit values produced by this generator is.
     *
     * @param n the positive bound on the random number to be returned.
     * @return the next pseudorandom {@code long} value between {@code 0} (inclusive) and {@code n} (exclusive).
     */
    public long nextLong(final long n) {
        if (n <= 0) throw new IllegalArgumentException("illegal bound " + n + " (must be positive)");
        long t = nextLong();
        final long nMinus1 = n - 1;
        // Shortcut for powers of two--high bits
        if ((n & nMinus1) == 0) return (t >>> Long.numberOfLeadingZeros(nMinus1)) & nMinus1;
        // Rejection-based algorithm to get uniform integers in the general case
        for (long u = t >>> 1; u + nMinus1 - (t = u % n) < 0; u = nextLong() >>> 1);
        return t;
    }

    @Override
    public double nextDouble() {
        return (nextLong() >>> 11) * 0x1.0p-53;
    }

    /**
     * Returns the next pseudorandom, uniformly distributed
     * {@code double} value between {@code 0.0} and
     * {@code 1.0} from this random number generator's sequence,
     * using a fast multiplication-free method which, however,
     * can provide only 52 significant bits.
     *
     * <p>This method is faster than {@link #nextDouble()}, but it
     * can return only dyadic rationals of the form <var>k</var> / 2<sup>&minus;52</sup>,
     * instead of the standard <var>k</var> / 2<sup>&minus;53</sup>. Before
     * version 2.4.1, this was actually the standard implementation of
     * {@link #nextDouble()}, so you can use this method if you need to
     * reproduce exactly results obtained using previous versions.
     *
     * <p>The only difference between the output of this method and that of
     * {@link #nextDouble()} is an additional least significant bit set in half of the
     * returned values. For most applications, this difference is negligible.
     *
     * @return the next pseudorandom, uniformly distributed {@code double}
     * value between {@code 0.0} and {@code 1.0} from this
     * random number generator's sequence, using 52 significant bits only.
     *
     * @since 2.4.1
     */
    public double nextDoubleFast() {
        return Double.longBitsToDouble(0x3FFL << 52 | nextLong() >>> 12) - 1.0;
    }

    @Override
    public float nextFloat() {
        return (nextLong() >>> 40) * 0x1.0p-24f;
    }

    @Override
    public boolean nextBoolean() {
        return nextLong() < 0;
    }

    @Override
    public void nextBytes(final byte[] bytes) {
        int i = bytes.length, n = 0;
        while(i != 0) {
            n = Math.min(i, 8);
            for (long bits = nextLong(); n-- != 0; bits >>= 8) bytes[--i] = (byte)bits;
        }
    }

    private static final long JUMP[] =     { 0xbeac0467eba5facbL, 0xd86b048b86aa9922L };

    /** The the jump function for this generator. It is equivalent to 2<sup>64</sup>
     * calls to {@link #nextLong()}; it can be used to generate 2<sup>64</sup>
     * non-overlapping subsequences for parallel computations. */

    public void jump() {
        long s0 = 0;
        long s1 = 0;
        for(int i = 0; i < JUMP.length; i++)
            for(int b = 0; b < 64; b++) {
                if ((JUMP[i] & 1L << b) != 0) {
                    s0 ^= this.s0;
                    s1 ^= this.s1;
                }
                nextLong();
            }

        this.s0 = s0;
        this.s1 = s1;
    }

    /**
     * Returns a new instance that shares no mutable state
     * with this instance. The sequence generated by the new instance
     * depends deterministically from the state of this instance,
     * but the probability that the sequence generated by this
     * instance and by the new instance overlap is negligible.
     *
     * @return the new instance.
     */
    public XoRoShiRo128PlusRandom split() {
        return new XoRoShiRo128PlusRandom(this);
    }

    /** Sets the seed of this generator.
     *
     * <p>The argument will be used to seed a {@link SplitMix64Random}, whose output
     * will in turn be used to seed this generator. This approach makes &ldquo;warmup&rdquo; unnecessary,
     * and makes the probability of starting from a state
     * with a large fraction of bits set to zero astronomically small.
     *
     * @param seed a seed for this generator.
     */
    @Override
    public void setSeed(final long seed) {
        final SplitMix64Random r = new SplitMix64Random(seed);
        s0 = r.nextLong();
        s1 = r.nextLong();
    }

    /** Sets the state of this generator.
     *
     * <p>The internal state of the generator will be reset, and the state array filled with the provided array.
     *
     * @param state an array of 2 longs; at least one must be nonzero.
     */
    public void setState(final long[] state) {
        if (state.length != 2) throw new IllegalArgumentException("The argument array contains " + state.length + " longs instead of " + 2);
        s0 = state[0];
        s1 = state[1];
    }
}
