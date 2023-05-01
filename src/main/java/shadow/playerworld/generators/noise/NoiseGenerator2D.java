package shadow.playerworld.generators.noise;

import java.util.Random;

public class NoiseGenerator2D {
    private static int[][] arrayI = new int[][]{{1, 1, 0}, {-1, 1, 0}, {1, -1, 0}, {-1, -1, 0}, {1, 0, 1}, {-1, 0, 1}, {1, 0, -1}, {-1, 0, -1}, {0, 1, 1}, {0, -1, 1}, {0, 1, -1}, {0, -1, -1}};
    private static final double const1 = 0.5 * (Math.sqrt(3.0) - 1.0);
    private static final double const2 = (3.0 - Math.sqrt(3.0)) / 6.0;
    public double randomDX;
    public double randomDZ;
    private int[] permutations = new int[512];

    private static int wrap(double d) {
        return d <= 0.0 ? (int)d - 1 : (int)d;
    }

    private static double method1(int[] ai, double d, double d1) {
        return (double)ai[0] * d + (double)ai[1] * d1;
    }

    public NoiseGenerator2D() {
        this(new Random());
    }

    public NoiseGenerator2D(Random random) {
        this.randomDX = random.nextDouble() * 256.0;
        this.randomDZ = random.nextDouble() * 256.0;
        double unused = random.nextDouble() * 256.0;
        for (int i = 0; i < 256; ++i) {
            this.permutations[i] = i;
        }
        for (int j = 0; j < 256; ++j) {
            int k = random.nextInt(256 - j) + j;
            int l = this.permutations[j];
            this.permutations[j] = this.permutations[k];
            this.permutations[k] = l;
            this.permutations[j + 256] = this.permutations[j];
        }
    }

    public void generateNoiseArray(double[] array, double xPos, double zPos, int xSize, int zSize, double gridX, double gridZ, double amplitude) {
        int k = 0;
        for (int x = 0; x < xSize; ++x) {
            double cx = (xPos + (double)x) * gridX + this.randomDX;
            for (int z = 0; z < zSize; ++z) {
                double d9;
                double d8;
                double d7;
                int i2;
                int l1;
                double d13;
                double d15;
                int k1;
                double d11;
                double cz = (zPos + (double)z) * gridZ + this.randomDZ;
                double d10 = (cx + cz) * const1;
                int j1 = NoiseGenerator2D.wrap(cx + d10);
                double d12 = (double)j1 - (d11 = (double)(j1 + (k1 = NoiseGenerator2D.wrap(cz + d10))) * const2);
                double d14 = cx - d12;
                if (d14 > (d15 = cz - (d13 = (double)k1 - d11))) {
                    l1 = 1;
                    i2 = 0;
                } else {
                    l1 = 0;
                    i2 = 1;
                }
                double d16 = d14 - (double)l1 + const2;
                double d17 = d15 - (double)i2 + const2;
                double d18 = d14 - 1.0 + 2.0 * const2;
                double d19 = d15 - 1.0 + 2.0 * const2;
                int j2 = j1 & 0xFF;
                int k2 = k1 & 0xFF;
                int l2 = this.permutations[j2 + this.permutations[k2]] % 12;
                int i3 = this.permutations[j2 + l1 + this.permutations[k2 + i2]] % 12;
                int j3 = this.permutations[j2 + 1 + this.permutations[k2 + 1]] % 12;
                double d20 = 0.5 - d14 * d14 - d15 * d15;
                if (d20 < 0.0) {
                    d7 = 0.0;
                } else {
                    d20 *= d20;
                    d7 = d20 * d20 * NoiseGenerator2D.method1(arrayI[l2], d14, d15);
                }
                double d21 = 0.5 - d16 * d16 - d17 * d17;
                if (d21 < 0.0) {
                    d8 = 0.0;
                } else {
                    d21 *= d21;
                    d8 = d21 * d21 * NoiseGenerator2D.method1(arrayI[i3], d16, d17);
                }
                double d22 = 0.5 - d18 * d18 - d19 * d19;
                if (d22 < 0.0) {
                    d9 = 0.0;
                } else {
                    d22 *= d22;
                    d9 = d22 * d22 * NoiseGenerator2D.method1(arrayI[j3], d18, d19);
                }
                int n = k++;
                array[n] = array[n] + 70.0 * (d7 + d8 + d9) * amplitude;
            }
        }
    }
}

