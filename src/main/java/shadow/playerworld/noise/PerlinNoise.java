package shadow.playerworld.noise;

import cn.nukkit.math.NukkitRandom;
import shadow.playerworld.noise.bukkit.PerlinNoiseGenerator;

public class PerlinNoise extends PerlinNoiseGenerator {
    public PerlinNoise(NukkitRandom rand) {
        int i;
        this.offsetX = rand.nextDouble() * 256.0;
        this.offsetY = rand.nextDouble() * 256.0;
        this.offsetZ = rand.nextDouble() * 256.0;
        for (i = 0; i < 256; ++i) {
            this.perm[i] = i;
        }
        for (i = 0; i < 256; ++i) {
            int pos = rand.nextBoundedInt(256 - i) + i;
            int old = this.perm[i];
            this.perm[i] = this.perm[pos];
            this.perm[pos] = old;
            this.perm[i + 256] = this.perm[i];
        }
    }

    public static int floor(double x) {
        int floored = (int)x;
        return x < (double)floored ? floored - 1 : floored;
    }

    public double[] getNoise(double[] noise, double x, double y, double z, int sizeX, int sizeY, int sizeZ, double scaleX, double scaleY, double scaleZ, double amplitude) {
        if (sizeY == 1) {
            return this.get2dNoise(noise, x, z, sizeX, sizeZ, scaleX, scaleZ, amplitude);
        }
        return this.get3dNoise(noise, x, y, z, sizeX, sizeY, sizeZ, scaleX, scaleY, scaleZ, amplitude);
    }

    protected double[] get2dNoise(double[] noise, double x, double z, int sizeX, int sizeZ, double scaleX, double scaleZ, double amplitude) {
        int index = 0;
        for (int i = 0; i < sizeX; ++i) {
            double dx = x + this.offsetX + (double)i * scaleX;
            int floorX = PerlinNoise.floor(dx);
            int ix = floorX & 0xFF;
            double fx = fade(dx -= (double)floorX);
            for (int j = 0; j < sizeZ; ++j) {
                double dz = z + this.offsetZ + (double)j * scaleZ;
                int floorZ = PerlinNoise.floor(dz);
                int iz = floorZ & 0xFF;
                double fz = fade(dz -= (double)floorZ);
                int a = this.perm[ix];
                int aa = this.perm[a] + iz;
                int b = this.perm[ix + 1];
                int ba = this.perm[b] + iz;
                double x1 = lerp(fx, grad(this.perm[aa], dx, 0.0, dz), grad(this.perm[ba], dx - 1.0, 0.0, dz));
                double x2 = lerp(fx, grad(this.perm[aa + 1], dx, 0.0, dz - 1.0), grad(this.perm[ba + 1], dx - 1.0, 0.0, dz - 1.0));
                int n = index++;
                noise[n] = noise[n] + lerp(fz, x1, x2) * amplitude;
            }
        }
        return noise;
    }

    protected double[] get3dNoise(double[] noise, double x, double y, double z, int sizeX, int sizeY, int sizeZ, double scaleX, double scaleY, double scaleZ, double amplitude) {
        int n = -1;
        double x1 = 0.0;
        double x2 = 0.0;
        double x3 = 0.0;
        double x4 = 0.0;
        int index = 0;
        for (int i = 0; i < sizeX; ++i) {
            double dx = x + this.offsetX + (double)i * scaleX;
            int floorX = PerlinNoise.floor(dx);
            int ix = floorX & 0xFF;
            double fx = fade(dx -= (double)floorX);
            for (int j = 0; j < sizeZ; ++j) {
                double dz = z + this.offsetZ + (double)j * scaleZ;
                int floorZ = PerlinNoise.floor(dz);
                int iz = floorZ & 0xFF;
                double fz = fade(dz -= (double)floorZ);
                for (int k = 0; k < sizeY; ++k) {
                    double dy = y + this.offsetY + (double)k * scaleY;
                    int floorY = PerlinNoise.floor(dy);
                    int iy = floorY & 0xFF;
                    double fy = fade(dy -= (double)floorY);
                    if (k == 0 || iy != n) {
                        n = iy;
                        int a = this.perm[ix] + iy;
                        int aa = this.perm[a] + iz;
                        int ab = this.perm[a + 1] + iz;
                        int b = this.perm[ix + 1] + iy;
                        int ba = this.perm[b] + iz;
                        int bb = this.perm[b + 1] + iz;
                        x1 = lerp(fx, grad(this.perm[aa], dx, dy, dz), grad(this.perm[ba], dx - 1.0, dy, dz));
                        x2 = lerp(fx, grad(this.perm[ab], dx, dy - 1.0, dz), grad(this.perm[bb], dx - 1.0, dy - 1.0, dz));
                        x3 = lerp(fx, grad(this.perm[aa + 1], dx, dy, dz - 1.0), grad(this.perm[ba + 1], dx - 1.0, dy, dz - 1.0));
                        x4 = lerp(fx, grad(this.perm[ab + 1], dx, dy - 1.0, dz - 1.0), grad(this.perm[bb + 1], dx - 1.0, dy - 1.0, dz - 1.0));
                    }
                    double y1 = lerp(fy, x1, x2);
                    double y2 = lerp(fy, x3, x4);
                    int n2 = index++;
                    noise[n2] = noise[n2] + lerp(fz, y1, y2) * amplitude;
                }
            }
        }
        return noise;
    }
}

