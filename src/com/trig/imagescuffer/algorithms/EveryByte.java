package com.trig.imagescuffer.algorithms;

import java.util.BitSet;
import java.util.Random;

public class EveryByte implements ScuffAlgorithm {

    private Random rand = new Random();

    @Override
    public byte[] rewriteBytes(byte[] buf) {
        BitSet bits = BitSet.valueOf(buf);
        for(int i = 0; i < bits.length() / 8; i++) {
            bits.flip(rand.nextInt(8) + 8 * i);
        }
        return bits.toByteArray();
    }
}
