package shadow.playerworld.generators.noise;

import java.util.Random;

public class FMB_RMF {
    private static final int[] ms_p = new int[512];

    public FMB_RMF(long seed) {
        int i;
        Random rand = new Random(seed);
        int nbVals = 256;
        int[] ms_perm = new int[256];
        for (i = 0; i < 256; ++i) {
            ms_perm[i] = -1;
        }
        i = 0;
        while (i < 256) {
            int p;
            while (ms_perm[p = rand.nextInt(256)] != -1) {
            }
            ms_perm[p] = i++;
        }
        for (i = 0; i < 256; ++i) {
            FMB_RMF.ms_p[256 + i] = FMB_RMF.ms_p[i] = ms_perm[i];
        }
    }

    private static double noise(double x, double y, double z) {
        int X = (int)x & 0xFF;
        int Y = (int)y & 0xFF;
        int Z = (int)z & 0xFF;
        x -= Math.floor(x);
        y -= Math.floor(y);
        z -= Math.floor(z);
        double u = FMB_RMF.fade(x);
        double v = FMB_RMF.fade(y);
        double w = FMB_RMF.fade(z);
        int A = ms_p[X] + Y;
        int AA = ms_p[A] + Z;
        int AB = ms_p[A + 1] + Z;
        int B = ms_p[X + 1] + Y;
        int BA = ms_p[B] + Z;
        int BB = ms_p[B + 1] + Z;
        return FMB_RMF.lerp(w, FMB_RMF.lerp(v, FMB_RMF.lerp(u, FMB_RMF.grad(ms_p[AA], x, y, z), FMB_RMF.grad(ms_p[BA], x - 1.0, y, z)), FMB_RMF.lerp(u, FMB_RMF.grad(ms_p[AB], x, y - 1.0, z), FMB_RMF.grad(ms_p[BB], x - 1.0, y - 1.0, z))), FMB_RMF.lerp(v, FMB_RMF.lerp(u, FMB_RMF.grad(ms_p[AA + 1], x, y, z - 1.0), FMB_RMF.grad(ms_p[BA + 1], x - 1.0, y, z - 1.0)), FMB_RMF.lerp(u, FMB_RMF.grad(ms_p[AB + 1], x, y - 1.0, z - 1.0), FMB_RMF.grad(ms_p[BB + 1], x - 1.0, y - 1.0, z - 1.0))));
    }

    private static double fade(double t) {
        return t * t * t * (t * (t * 6.0 - 15.0) + 10.0);
    }

    private static double lerp(double t, double a, double b) {
        return a + t * (b - a);
    }

    private static double grad(int hash, double x, double y, double z) {
        double u;
        int h = hash & 0xF;
        double d = u = h < 8 ? x : y;
        double v = h < 4 ? y : (h == 12 || h == 14 ? x : z);
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }

    public static double ridge(double h, float offset) {
        h = Math.abs(h);
        h = (double)offset - h;
        h *= h;
        return h;
    }

    public double noise_RidgedMultiFractal(double x, double y, double z, int octaves, float lacunarity, float gain, float offset) {
        double sum = 0.0;
        float amplitude = 0.5f;
        float frequency = 1.0f;
        double prev = 1.0;
        for (int i = 0; i < octaves; ++i) {
            double n = FMB_RMF.ridge(FMB_RMF.noise(x * (double)frequency, y * (double)frequency, z * (double)frequency), offset);
            sum += n * (double)amplitude * prev;
            prev = n;
            frequency *= lacunarity;
            amplitude *= gain;
        }
        return sum;
    }

    public double noise_FractionalBrownianMotion(double x, double y, double z, int octaves, float lacunarity, float gain) {
        double frequency = 1.0;
        double amplitude = 0.5;
        double sum = 0.0;
        for (int i = 0; i < octaves; ++i) {
            sum += FMB_RMF.noise(x * frequency, y * frequency, z * frequency) * amplitude;
            frequency *= (double)lacunarity;
            amplitude *= (double)gain;
        }
        return sum;
    }
}

