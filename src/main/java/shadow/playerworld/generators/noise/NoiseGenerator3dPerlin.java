package shadow.playerworld.generators.noise;

import java.util.Random;

public class NoiseGenerator3dPerlin {
    public double randomDX;
    public double randomDY;
    public double randomDZ;
    private int[] permutations = new int[512];
    private final boolean nofarlands;

    public NoiseGenerator3dPerlin(Random random, boolean nofarlands) {
        this.randomDX = random.nextDouble() * 256.0;
        this.randomDY = random.nextDouble() * 256.0;
        this.randomDZ = random.nextDouble() * 256.0;
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
        this.nofarlands = nofarlands;
    }

    public double generateNoise(double xPos, double yPos, double zPos) {
        double x = xPos + this.randomDX;
        double y = yPos + this.randomDY;
        double z = zPos + this.randomDZ;
        if (this.nofarlands) {
            x = this.nofarlands(x);
            y = this.nofarlands(y);
            z = this.nofarlands(z);
        }
        int intX = (int)x;
        int intY = (int)y;
        int intZ = (int)z;
        if (x < (double)intX) {
            --intX;
        }
        if (y < (double)intY) {
            --intY;
        }
        if (z < (double)intZ) {
            --intZ;
        }
        int p1 = intX & 0xFF;
        int p2 = intY & 0xFF;
        int p3 = intZ & 0xFF;
        double fx = (x -= (double)intX) * x * x * (x * (x * 6.0 - 15.0) + 10.0);
        double fy = (y -= (double)intY) * y * y * (y * (y * 6.0 - 15.0) + 10.0);
        double fz = (z -= (double)intZ) * z * z * (z * (z * 6.0 - 15.0) + 10.0);
        int a1 = this.permutations[p1] + p2;
        int a2 = this.permutations[a1] + p3;
        int a3 = this.permutations[a1 + 1] + p3;
        int a4 = this.permutations[p1 + 1] + p2;
        int a5 = this.permutations[a4] + p3;
        int a6 = this.permutations[a4 + 1] + p3;
        return this.lerp(fz, this.lerp(fy, this.lerp(fx, this.grad3d(this.permutations[a2], x, y, z), this.grad3d(this.permutations[a5], x - 1.0, y, z)), this.lerp(fx, this.grad3d(this.permutations[a3], x, y - 1.0, z), this.grad3d(this.permutations[a6], x - 1.0, y - 1.0, z))), this.lerp(fy, this.lerp(fx, this.grad3d(this.permutations[a2 + 1], x, y, z - 1.0), this.grad3d(this.permutations[a5 + 1], x - 1.0, y, z - 1.0)), this.lerp(fx, this.grad3d(this.permutations[a3 + 1], x, y - 1.0, z - 1.0), this.grad3d(this.permutations[a6 + 1], x - 1.0, y - 1.0, z - 1.0))));
    }

    public final double lerp(double d, double d1, double d2) {
        return d1 + d * (d2 - d1);
    }

    public final double grad2d(int i, double x, double z) {
        int j = i & 0xF;
        double d2 = (double)(1 - ((j & 8) >> 3)) * x;
        double d3 = j >= 4 ? (j != 12 && j != 14 ? z : x) : 0.0;
        return ((j & 1) != 0 ? -d2 : d2) + ((j & 2) != 0 ? -d3 : d3);
    }

    public final double grad3d(int i, double x, double y, double z) {
        double d3;
        int j = i & 0xF;
        double d = d3 = j >= 8 ? y : x;
        double d4 = j >= 4 ? (j != 12 && j != 14 ? z : x) : y;
        return ((j & 1) != 0 ? -d3 : d3) + ((j & 2) != 0 ? -d4 : d4);
    }

    public double generateNoise(double d, double d1) {
        return this.generateNoise(d, d1, 0.0);
    }

    public void generateNoiseArray(double[] array, double xPos, double yPos, double zPos, int xSize, int ySize, int zSize, double gridX, double gridY, double gridZ, double a) {
        if (ySize == 1) {
            int index = 0;
            double amplitude = 1.0 / a;
            for (int dx = 0; dx < xSize; ++dx) {
                int intX;
                double x = (xPos + (double)dx) * gridX + this.randomDX;
                if (this.nofarlands) {
                    x = this.nofarlands(x);
                }
                if (x < (double)(intX = (int)x)) {
                    --intX;
                }
                int k4 = intX & 0xFF;
                double d17 = (x -= (double)intX) * x * x * (x * (x * 6.0 - 15.0) + 10.0);
                for (int dz = 0; dz < zSize; ++dz) {
                    int intZ;
                    double z = (zPos + (double)dz) * gridZ + this.randomDZ;
                    if (this.nofarlands) {
                        z = this.nofarlands(z);
                    }
                    if (z < (double)(intZ = (int)z)) {
                        --intZ;
                    }
                    int l5 = intZ & 0xFF;
                    double d21 = (z -= (double)intZ) * z * z * (z * (z * 6.0 - 15.0) + 10.0);
                    int l = this.permutations[k4] + 0;
                    int j1 = this.permutations[l] + l5;
                    int k1 = this.permutations[k4 + 1] + 0;
                    int l1 = this.permutations[k1] + l5;
                    double d9 = this.lerp(d17, this.grad2d(this.permutations[j1], x, z), this.grad3d(this.permutations[l1], x - 1.0, 0.0, z));
                    double d11 = this.lerp(d17, this.grad3d(this.permutations[j1 + 1], x, 0.0, z - 1.0), this.grad3d(this.permutations[l1 + 1], x - 1.0, 0.0, z - 1.0));
                    double value = this.lerp(d21, d9, d11);
                    int n = index++;
                    array[n] = array[n] + value * amplitude;
                }
            }
            return;
        }
        int i1 = 0;
        double amplitude = 1.0 / a;
        int i2 = -1;
        double d13 = 0.0;
        double d15 = 0.0;
        double d16 = 0.0;
        double d18 = 0.0;
        for (int dx = 0; dx < xSize; ++dx) {
            int intX;
            double x = (xPos + (double)dx) * gridX + this.randomDX;
            if (this.nofarlands) {
                x = this.nofarlands(x);
            }
            if (x < (double)(intX = (int)x)) {
                --intX;
            }
            int i6 = intX & 0xFF;
            double d22 = (x -= (double)intX) * x * x * (x * (x * 6.0 - 15.0) + 10.0);
            for (int dz = 0; dz < zSize; ++dz) {
                int k6;
                double z = (zPos + (double)dz) * gridZ + this.randomDZ;
                if (this.nofarlands) {
                    z = this.nofarlands(z);
                }
                if (z < (double)(k6 = (int)z)) {
                    --k6;
                }
                int l6 = k6 & 0xFF;
                double d25 = (z -= (double)k6) * z * z * (z * (z * 6.0 - 15.0) + 10.0);
                for (int dy = 0; dy < ySize; ++dy) {
                    double y = (yPos + (double)dy) * gridY + this.randomDY;
                    int j7 = (int)y;
                    if (y < (double)j7) {
                        --j7;
                    }
                    int k7 = j7 & 0xFF;
                    double d27 = (y -= (double)j7) * y * y * (y * (y * 6.0 - 15.0) + 10.0);
                    if (dy == 0 || k7 != i2) {
                        i2 = k7;
                        int j2 = this.permutations[i6] + k7;
                        int k2 = this.permutations[j2] + l6;
                        int l2 = this.permutations[j2 + 1] + l6;
                        int i3 = this.permutations[i6 + 1] + k7;
                        int k3 = this.permutations[i3] + l6;
                        int l3 = this.permutations[i3 + 1] + l6;
                        d13 = this.lerp(d22, this.grad3d(this.permutations[k2], x, y, z), this.grad3d(this.permutations[k3], x - 1.0, y, z));
                        d15 = this.lerp(d22, this.grad3d(this.permutations[l2], x, y - 1.0, z), this.grad3d(this.permutations[l3], x - 1.0, y - 1.0, z));
                        d16 = this.lerp(d22, this.grad3d(this.permutations[k2 + 1], x, y, z - 1.0), this.grad3d(this.permutations[k3 + 1], x - 1.0, y, z - 1.0));
                        d18 = this.lerp(d22, this.grad3d(this.permutations[l2 + 1], x, y - 1.0, z - 1.0), this.grad3d(this.permutations[l3 + 1], x - 1.0, y - 1.0, z - 1.0));
                    }
                    double d28 = this.lerp(d27, d13, d15);
                    double d29 = this.lerp(d27, d16, d18);
                    double value = this.lerp(d25, d28, d29);
                    int n = i1++;
                    array[n] = array[n] + value * amplitude;
                }
            }
        }
    }

    private double nofarlands(double a) {
        while (a > 2.147483647E9) {
            a -= 4.294967295E9;
        }
        while (a < -2.147483648E9) {
            a += 4.294967295E9;
        }
        return a;
    }
}

