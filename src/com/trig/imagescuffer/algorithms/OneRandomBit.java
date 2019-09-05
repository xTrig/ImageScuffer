package com.trig.imagescuffer.algorithms;

import java.util.BitSet;
import java.util.Random;

public class OneRandomBit implements ScuffAlgorithm {

    private Random rand = new Random();

    @Override
    public byte[] rewriteBytes(byte[] buf) {
        BitSet bits = BitSet.valueOf(buf);
        bits.flip(rand.nextInt(bits.length()));
        return bits.toByteArray();
    }
}
